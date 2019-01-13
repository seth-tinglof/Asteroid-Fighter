
import java.io.File;                //allows getting audio file.
import java.io.IOException;

import javax.sound.sampled.*;       //allows for audio playback 
/**
 * Plays level background music and sfx.
 * 
 * @author Seth Tinglof 
 * @version 1.0
 */
public class Sound implements Runnable{              //allows music to be controlled by a separate thread

    public static final int MUSIC_VOLUME = -20;
    public static final int SHOT_VOLUME = -15;

    /**
     * plays sound for the player shooting.
     */
    public static void playerShot()
    {
        try
        {
            Clip clip;
            File file;
            AudioInputStream audioInput; 
            file = new File("shot.wav");
            audioInput = AudioSystem.getAudioInputStream(file);        //creates audio input stream from the audio file
            clip = AudioSystem.getClip();                              //creates audio clip
            clip.open(audioInput);
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(SHOT_VOLUME);
            clip.start();
        }catch(LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            System.out.println("Unable to play bullet fired sound: " + e.getMessage());
        }
    }
    
    int track = 1;
    long trackLength;
    boolean isPlaying = true;
    File audioTrack;
    Clip clip;
    
    
    /**
     * Plays and loops track.
     */
    public void run()
    {
        isPlaying = true;
        int numTracks = 0;
        do{
        	try{
        		File[] files = new File("music").listFiles();
        		numTracks = files.length;
        		if(numTracks == 0)
        		    return;
        		audioTrack = files[track - 1];
                AudioInputStream trackPlaying = AudioSystem.getAudioInputStream(audioTrack);        //creates audio input stream from the audio file
                clip = AudioSystem.getClip();                                                       //creates audio clip
                clip.open(trackPlaying);                                                            //sets audioclip to input stream from audio file
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(MUSIC_VOLUME);
                trackLength = clip.getMicrosecondLength() / 1000;
                clip.start();
                Thread.sleep(trackLength);
        	}catch(LineUnavailableException | InterruptedException | IOException | UnsupportedAudioFileException e) {
        	    System.out.println("Unable to play music: " + e.getMessage());
            }
        	track++;
        	if(track > numTracks)
        		track = 1;
        }while(isPlaying);
    }
    
    /**
     * Stops currently playing track.
     */
    public void stopTrack()
    {
        clip.stop();
        isPlaying = false;
    }
}