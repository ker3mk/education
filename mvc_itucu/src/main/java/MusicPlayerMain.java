package main.java;



public class MusicPlayerMain {

    public static void main (String[] args) {
        MusicModelInterface model = new MusicModel();
		ControllerInterface controller = new BeatController(model);
    }
}
