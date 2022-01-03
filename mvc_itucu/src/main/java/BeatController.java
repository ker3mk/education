package main.java;

public class BeatController implements ControllerInterface {
	MusicModelInterface model;
	MusicPlayerGui view;
   
	public BeatController(MusicModelInterface model) {
		this.model = model;
		view = new MusicPlayerGui(this, model);
        view.createView();
        view.createControls();
		view.disableStopMenuItem();
		view.enableStartMenuItem();
		model.initialize();
	}
  
	public void start() {
		model.on();
		view.disableStartMenuItem();
		view.enableStopMenuItem();
	}
  
	public void stop() {
		model.off();
		view.disableStopMenuItem();
		view.enableStartMenuItem();
	}
    
	public void increaseTempo() {
        int bpm = model.getTempo();
        model.setTempo(bpm + 1);
	}
    
	public void decreaseTempo() {
        int bpm = model.getTempo();
        model.setTempo(bpm - 1);
  	}
  
 	public void setTempo(int tempo) {
		model.setTempo(tempo);
	}
}
