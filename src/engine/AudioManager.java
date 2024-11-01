package engine;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.stb.STBVorbis;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.openal.AL10.*;
import org.lwjgl.system.MemoryUtil;

public class AudioManager {
    private long device;
    private long context;
    private final Map<String, Integer> audioBuffers = new HashMap<>();
    private final Map<String, Integer> audioSources = new HashMap<>();
    private final Map<String, Runnable> endCallbacks = new HashMap<>();

    public void init() {
        device = ALC11.alcOpenDevice((String) null);
        if (device == 0) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }

        context = ALC11.alcCreateContext(device, (IntBuffer) null);
        ALC11.alcMakeContextCurrent(context);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        AL.createCapabilities(alcCapabilities);
    }

    public void cleanup() {
        for (Integer buffer : audioBuffers.values()) {
            alDeleteBuffers(buffer);
        }
        for (Integer source : audioSources.values()) {
            alDeleteSources(source);
        }
        audioBuffers.clear();
        audioSources.clear();
        endCallbacks.clear();
        ALC11.alcDestroyContext(context);
        ALC11.alcCloseDevice(device);
    }

    public void addAudio(String name, String filePath) {
        int buffer = alGenBuffers();
        int source = alGenSources();
        loadOGG(buffer, filePath);
        audioBuffers.put(name, buffer);
        audioSources.put(name, source);
        alSourcei(source, AL_BUFFER, buffer);
        alSourcef(source, AL_GAIN, 1.0f);
    }


    private void loadOGG(int buffer, String filePath) {
        IntBuffer channels = MemoryUtil.memAllocInt(1);
        IntBuffer sampleRate = MemoryUtil.memAllocInt(1);
        try (InputStream stream = AudioManager.class.getResourceAsStream(filePath)) {
            if (stream == null) {
                throw new RuntimeException("Failed to load OGG file from JAR: " + filePath);
            }
            ByteBuffer audioBuffer = MemoryUtil.memAlloc(stream.available());
            Channels.newChannel(stream).read(audioBuffer);
            audioBuffer.flip();
            ShortBuffer vorbisData = STBVorbis.stb_vorbis_decode_memory(audioBuffer, channels, sampleRate);
            if (vorbisData == null) {
                throw new RuntimeException("Failed to decode OGG data.");
            }
            ByteBuffer byteBuffer = MemoryUtil.memAlloc(vorbisData.remaining() * 2);
            for (int i = 0; i < vorbisData.remaining(); i++) {
                byteBuffer.putShort(vorbisData.get(i));
            }
            byteBuffer.flip();
            alBufferData(buffer, channels.get(0) == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, byteBuffer, sampleRate.get(0));
            MemoryUtil.memFree(vorbisData);
            MemoryUtil.memFree(byteBuffer);
            MemoryUtil.memFree(audioBuffer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read OGG file from JAR.", e);
        } finally {
            MemoryUtil.memFree(channels);
            MemoryUtil.memFree(sampleRate);
        }
    }

    public void playSound(String name, Runnable callback) {
        Integer source = audioSources.get(name);
        if (source != null) {
            alSourcePlay(source);
            if (callback != null) {
                endCallbacks.put(name, callback);
            }
        }
    }

    public void update() {
        for (String name : audioSources.keySet()) {
            Integer source = audioSources.get(name);
            if (source != null) {
                int state = alGetSourcei(source, AL_SOURCE_STATE);
                if (state == AL_STOPPED) {
                    Runnable callback = endCallbacks.remove(name);
                    if (callback != null) {
                        callback.run();
                    }
                }
            }
        }
    }

    public void pause(String name) {
        Integer source = audioSources.get(name);
        if (source != null) {
            alSourcePause(source);
        }
    }


    public void stop(String name) {
        Integer source = audioSources.get(name);
        if (source != null) {
            alSourceStop(source);
            alSourcei(source, AL11.AL_SAMPLE_OFFSET, 0);
            endCallbacks.remove(name); // Clear the callback if stopped manually
        }
    }

    public boolean isPlaying(String name) {
        Integer source = audioSources.get(name);
        if (source != null) {
            return alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING;
        }
        return false;
    }

    public void setLoop(String name, boolean loop) {
        Integer source = audioSources.get(name);
        if (source != null) {
            alSourcei(source, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
        }
    }

    public void setPitch(String name, int pitch){
        Integer source = audioSources.get(name);
        if(source != null){
            alSourcei(source, AL_PITCH, pitch);
        }
    }

    public void setVolume(String name, float volume) {
        Integer source = audioSources.get(name);
        if (source != null) {
            alSourcef(source, AL_GAIN, volume);
        }
    }

    public float getTime(String name) {
        Integer source = audioSources.get(name);
        if (source != null) {
            return alGetSourcef(source, AL11.AL_SEC_OFFSET);
        }
        return 0;
    }

}
