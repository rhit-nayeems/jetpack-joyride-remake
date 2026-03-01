package mainApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

/**
 * Class: SoundManager
 * Purpose: play smoother synthesized sound effects with consistent sequencing
 */
public class SoundManager {

	private static final float SAMPLE_RATE = 44100f;
	private static final double MASTER_VOLUME = 0.09;
	private static final long COIN_COOLDOWN_NS = 60_000_000L;

	private static class Tone {
		double frequency;
		int durationMs;
		double gain;

		Tone(double frequency, int durationMs, double gain) {
			this.frequency = frequency;
			this.durationMs = durationMs;
			this.gain = gain;
		}
	}

	private final ExecutorService audioExecutor;
	private volatile boolean enabled;
	private long lastCoinSoundNs;

	public SoundManager() {
		this.enabled = true;
		this.lastCoinSoundNs = 0;
		this.audioExecutor = Executors.newSingleThreadExecutor(runnable -> {
			Thread t = new Thread(runnable, "sound-manager");
			t.setDaemon(true);
			return t;
		});
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void playCoin() {
		long now = System.nanoTime();
		if (now - lastCoinSoundNs < COIN_COOLDOWN_NS) {
			return;
		}
		lastCoinSoundNs = now;
		playSequenceAsync(new Tone(880, 45, 0.85), new Tone(1174, 55, 0.72));
	}

	public void playHit() {
		playSequenceAsync(new Tone(330, 75, 0.85), new Tone(247, 95, 0.75));
	}

	public void playGameOver() {
		playSequenceAsync(new Tone(392, 90, 0.7), new Tone(294, 110, 0.72), new Tone(220, 180, 0.76));
	}

	public void playLevelUp() {
		playSequenceAsync(new Tone(523, 70, 0.8), new Tone(659, 70, 0.78), new Tone(784, 95, 0.74));
	}

	public void playPauseToggle() {
		playSequenceAsync(new Tone(523, 55, 0.58));
	}

	private void playSequenceAsync(Tone... tones) {
		if (!enabled || tones == null || tones.length == 0) {
			return;
		}
		audioExecutor.execute(() -> playSequence(tones));
	}

	private void playSequence(Tone... tones) {
		AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
		try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
			line.open(format);
			line.start();
			for (Tone tone : tones) {
				writeTone(line, tone.frequency, tone.durationMs, tone.gain * MASTER_VOLUME);
			}
			line.drain();
		} catch (Exception e) {
			// Silent fallback: avoid platform beeps that sound harsher than the intended effects.
		}
	}

	private void writeTone(SourceDataLine line, double frequency, int durationMs, double gain) {
		int frames = Math.max(1, (int) (durationMs * SAMPLE_RATE / 1000.0));
		int attackFrames = Math.max(8, frames / 10);
		int releaseFrames = Math.max(12, frames / 6);

		byte[] buffer = new byte[frames * 2];
		for (int frame = 0; frame < frames; frame++) {
			double phase = 2.0 * Math.PI * frame * frequency / SAMPLE_RATE;
			double sample = Math.sin(phase) + 0.25 * Math.sin(2.0 * phase) + 0.08 * Math.sin(3.0 * phase);
			double normalized = sample / 1.33;

			double envelope = 1.0;
			if (frame < attackFrames) {
				envelope = frame / (double) attackFrames;
			} else if (frame > frames - releaseFrames) {
				envelope = (frames - frame) / (double) releaseFrames;
			}

			short pcm = (short) (normalized * 32767 * gain * Math.max(0.0, Math.min(1.0, envelope)));
			buffer[frame * 2] = (byte) (pcm & 0xFF);
			buffer[frame * 2 + 1] = (byte) ((pcm >>> 8) & 0xFF);
		}

		line.write(buffer, 0, buffer.length);
	}
}
