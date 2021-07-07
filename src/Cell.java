
public class Cell {
	boolean isMine;
	int minesNear;
	
	
	

	public int getMinesNear() {
		return minesNear;
	}

	public void setMinesNear(int minesNear) {
		this.minesNear = minesNear;
	}

	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}
	
	public Cell(boolean isMine) {
		setMine(isMine);
	}
	
}
