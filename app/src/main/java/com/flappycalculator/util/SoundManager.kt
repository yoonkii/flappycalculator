package com.flappycalculator.util

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Manages game sound effects.
 * Uses ToneGenerator for synthesized sounds that work immediately.
 * Can be extended to use SoundPool with actual audio files.
 */
class SoundManager(context: Context) {

    private val appContext = context.applicationContext

    private val toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    } catch (e: Exception) {
        null
    }

    private val soundPool: SoundPool
    private val sounds = mutableMapOf<Sound, Int>()
    private var isLoaded = false
    private val scope = CoroutineScope(Dispatchers.Default)

    private var bgmPlayer: MediaPlayer? = null
    private var gameOverPlayer: MediaPlayer? = null
    private var fadeOutJob: kotlinx.coroutines.Job? = null
    private val bgmFiles = listOf(
        "bgm/flappy_calculator_bgm_1.wav",
        "bgm/flappy_calculator_bgm_2.wav"
    )
    private val gameOverMusic = "bgm/Game Over, Try Again.wav"

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                isLoaded = true
            }
        }

        // Try to load sound files if they exist
        tryLoadSounds(context)
    }

    private fun tryLoadSounds(context: Context) {
        try {
            // Check if sound resources exist and load them
            val packageName = context.packageName
            val resources = context.resources

            // Try to load each sound - if resource doesn't exist, it will throw
            val flapId = resources.getIdentifier("flap", "raw", packageName)
            val correctId = resources.getIdentifier("correct", "raw", packageName)
            val wrongId = resources.getIdentifier("wrong", "raw", packageName)
            val hitId = resources.getIdentifier("hit", "raw", packageName)
            val scoreId = resources.getIdentifier("score", "raw", packageName)

            if (flapId != 0) sounds[Sound.FLAP] = soundPool.load(context, flapId, 1)
            if (correctId != 0) sounds[Sound.CORRECT] = soundPool.load(context, correctId, 1)
            if (wrongId != 0) sounds[Sound.WRONG] = soundPool.load(context, wrongId, 1)
            if (hitId != 0) sounds[Sound.HIT] = soundPool.load(context, hitId, 1)
            if (scoreId != 0) sounds[Sound.SCORE] = soundPool.load(context, scoreId, 1)

            isLoaded = sounds.isNotEmpty()
        } catch (e: Exception) {
            // Sound files not available, will use ToneGenerator fallback
            isLoaded = false
        }
    }

    /**
     * Play the flap sound (short whoosh).
     */
    fun playFlap() {
        if (playSoundPool(Sound.FLAP)) return
        // Fallback: short beep
        playTone(ToneGenerator.TONE_PROP_BEEP, 50)
    }

    /**
     * Play the correct answer sound (pleasant ding).
     */
    fun playCorrect() {
        if (playSoundPool(Sound.CORRECT)) return
        // Fallback: ascending tone
        playTone(ToneGenerator.TONE_PROP_ACK, 100)
    }

    /**
     * Play the wrong answer sound (soft buzz).
     */
    fun playWrong() {
        if (playSoundPool(Sound.WRONG)) return
        // Fallback: error tone
        playTone(ToneGenerator.TONE_PROP_NACK, 150)
    }

    /**
     * Play the hit/collision sound (thud).
     */
    fun playHit() {
        if (playSoundPool(Sound.HIT)) return
        // Fallback: low tone
        playTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 200)
    }

    /**
     * Play the score increment sound (coin/point sound).
     */
    fun playScore() {
        if (playSoundPool(Sound.SCORE)) return
        // Fallback: short positive beep
        playTone(ToneGenerator.TONE_CDMA_CONFIRM, 80)
    }

    /**
     * Play a key press sound (short click).
     */
    fun playKeyPress() {
        // Short, subtle click sound for key presses
        playTone(ToneGenerator.TONE_PROP_BEEP2, 30)
    }

    /**
     * Play a sound from the SoundPool if available.
     * @return true if sound was played, false otherwise
     */
    private fun playSoundPool(sound: Sound, volume: Float = 1f): Boolean {
        if (!isLoaded) return false

        val soundId = sounds[sound] ?: return false
        soundPool.play(
            soundId,
            volume,  // left volume
            volume,  // right volume
            1,       // priority
            0,       // loop (0 = no loop)
            1f       // rate
        )
        return true
    }

    /**
     * Play a tone using ToneGenerator.
     */
    private fun playTone(toneType: Int, durationMs: Int) {
        scope.launch {
            try {
                toneGenerator?.startTone(toneType, durationMs)
            } catch (e: Exception) {
                // Ignore tone playback errors
            }
        }
    }

    /**
     * Start playing a randomly selected BGM track, looping.
     */
    fun startBgm() {
        stopBgm()
        try {
            val track = bgmFiles.random()
            val afd: AssetFileDescriptor = appContext.assets.openFd(track)
            bgmPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                isLooping = true
                setVolume(0.5f, 0.5f)
                prepare()
                start()
            }
        } catch (e: Exception) {
            // BGM not critical — ignore errors
            bgmPlayer?.release()
            bgmPlayer = null
        }
    }

    /**
     * Play the game over music (single play, no loop).
     */
    fun playGameOverMusic() {
        stopGameOverMusic(immediate = true)
        try {
            val afd: AssetFileDescriptor = appContext.assets.openFd(gameOverMusic)
            gameOverPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                isLooping = false
                setVolume(0.6f, 0.6f)
                prepare()
                start()
            }
            // Auto fade-out after 3 seconds
            stopGameOverMusic(immediate = false)
        } catch (e: Exception) {
            gameOverPlayer?.release()
            gameOverPlayer = null
        }
    }

    /**
     * Stop game over music with a 3-second fade-out, or immediately.
     */
    fun stopGameOverMusic(immediate: Boolean = false) {
        fadeOutJob?.cancel()
        if (immediate) {
            releaseGameOverPlayer()
        } else {
            val player = gameOverPlayer ?: return
            fadeOutJob = scope.launch {
                val steps = 30
                val intervalMs = 3000L / steps
                for (i in steps downTo 0) {
                    try {
                        val vol = (i.toFloat() / steps) * 0.6f
                        player.setVolume(vol, vol)
                    } catch (_: Exception) { break }
                    kotlinx.coroutines.delay(intervalMs)
                }
                releaseGameOverPlayer()
            }
        }
    }

    private fun releaseGameOverPlayer() {
        try {
            gameOverPlayer?.apply {
                if (isPlaying) stop()
                release()
            }
        } catch (_: Exception) { }
        gameOverPlayer = null
    }

    /**
     * Stop BGM playback.
     */
    fun stopBgm() {
        try {
            bgmPlayer?.apply {
                if (isPlaying) stop()
                release()
            }
        } catch (_: Exception) { }
        bgmPlayer = null
    }

    /**
     * Release resources.
     * Call when the game is destroyed.
     */
    fun pauseAll() {
        try { bgmPlayer?.takeIf { it.isPlaying }?.pause() } catch (_: Exception) { }
        try { gameOverPlayer?.takeIf { it.isPlaying }?.pause() } catch (_: Exception) { }
    }

    fun resumeAll() {
        try { bgmPlayer?.start() } catch (_: Exception) { }
        try { gameOverPlayer?.start() } catch (_: Exception) { }
    }

    fun stopAll() {
        stopBgm()
        stopGameOverMusic(immediate = true)
    }

    fun release() {
        stopAll()
        soundPool.release()
        sounds.clear()
        toneGenerator?.release()
    }

    /**
     * Sound effect types.
     */
    enum class Sound {
        FLAP,
        CORRECT,
        WRONG,
        HIT,
        SCORE
    }
}
