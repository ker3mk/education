package main.java;

import javax.sound.midi.*;
import java.util.ArrayList;

public class MusicModel implements MusicModelInterface, MetaEventListener {
	Sequencer sequencer;
	ArrayList<MusicTempoObserver> musicTempoObservers = new ArrayList<MusicTempoObserver>();
	int bpm = 90;
	Sequence sequence;
	Track track;

	public void initialize() {
		setUpMidi();
		buildTrackAndStart();
	}

	public void on() {
		System.out.println("Starting the sequencer");
		sequencer.start();
		setTempo(90);
	}

	public void off() {
		setTempo(0);
		sequencer.stop();
	}

	public void setTempo(int bpm) {
		this.bpm = bpm;
		sequencer.setTempoInBPM(getTempo());
		notifyBPMObservers();
	}

	public int getTempo() {
		return bpm;
	}



	public void registerObserver(MusicTempoObserver o) {
		musicTempoObservers.add(o);
	}

	public void notifyBPMObservers() {
		for(int i = 0; i < musicTempoObservers.size(); i++) {
			MusicTempoObserver observer = (MusicTempoObserver) musicTempoObservers.get(i);
			observer.updateTempo();
		}
	}




	public void removeObserver(MusicTempoObserver o) {
		int i = musicTempoObservers.indexOf(o);
		if (i >= 0) {
			musicTempoObservers.remove(i);
		}
	}


	public void meta(MetaMessage message) {
		if (message.getType() == 47) {
			sequencer.start();
			setTempo(getTempo());
		}
	}

	public void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.addMetaEventListener(this);
			sequence = new Sequence(Sequence.PPQ,4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(getTempo());
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		} catch(Exception e) {
			e.printStackTrace();
		}
	} 

	public void buildTrackAndStart() {
		int[] trackList = {35, 0, 46, 0};

		sequence.deleteTrack(null);
		track = sequence.createTrack();

		makeTracks(trackList);
		track.add(makeEvent(192,9,1,0,4));      
		try {
			sequencer.setSequence(sequence);                    
		} catch(Exception e) {
			e.printStackTrace();
		}
	} 

	public void makeTracks(int[] list) {        

		for (int i = 0; i < list.length; i++) {
			int key = list[i];

			if (key != 0) {
				track.add(makeEvent(144,9,key, 100, i));
				track.add(makeEvent(128,9,key, 100, i+1));
			}
		}
	}

	public  MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);

		} catch(Exception e) {
			e.printStackTrace(); 
		}
		return event;
	}
}
