package main.java;

public interface MusicModelInterface {
	void initialize();
  
	void on();
  
	void off();
  
    void setTempo(int bpm);
  
	int getTempo();
  
	void registerObserver(MusicTempoObserver o);
  
	void removeObserver(MusicTempoObserver o);
}
