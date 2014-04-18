package dblockcache;

import common.DFileID;

public class CBuffer {
	private boolean isBusy;
	private boolean isClean;
	private boolean ioComplete;
	private byte[] buffer;
	private DFileID fileID;
	private int blockID;
	
	public void setFileID(DFileID newFileID) {
		fileID = newFileID;
	}
	
	public void setBlockID(int block) {
		blockID = block;
	}
	
	/* Start an asynchronous fetch of associated block from the volume */
	public void startFetch() {
		
	}
	
	/* Start an asynchronous write of buffer contents to block on volume */
	public void startPush(){
		
	}
	
	/* Check whether the buffer has valid data */ 
	public boolean checkValid(){
		
	}
	
	/* Wait until the buffer has valid data, i.e., wait for fetch to complete */
	public boolean waitValid(){
		
	}
	
	/* Check whether the buffer is dirty, i.e., has modified data written back to disk? */
	public boolean checkClean(){
		return isClean;
	}
	
	/* Wait until the buffer is clean, i.e., wait until a push operation completes */
	public boolean waitClean(){
		
	}
	
	/* Check if buffer is evictable: not evictable if I/O in progress, or buffer is held */
	public boolean isBusy(){
		return isBusy;
	}

	/*
	 * reads into the buffer[] array from the contents of the DBuffer. Check
	 * first that the DBuffer has a valid copy of the data! startOffset and
	 * count are for the buffer array, not the DBuffer. Upon an error, it should
	 * return -1, otherwise return number of bytes read.
	 */
	public int read(byte[] buffer, int startOffset, int count){
		
	}

	/*
	 * writes into the DBuffer from the contents of buffer[] array. startOffset
	 * and count are for the buffer array, not the DBuffer. Mark buffer dirty!
	 * Upon an error, it should return -1, otherwise return number of bytes
	 * written.
	 */
	public int write(byte[] buffer, int startOffset, int count){
		
	}
	
	/* An upcall from VirtualDisk layer to inform the completion of an IO operation */
	public void ioComplete(){
		ioComplete = true;
	}
	
	/* An upcall from VirtualDisk layer to fetch the blockID associated with a startRequest operation */
	public int getBlockID(){
		
	}
	
	/* An upcall from VirtualDisk layer to fetch the buffer associated with DBuffer object*/
	public byte[] getBuffer(){
		return buffer;
	}
}
