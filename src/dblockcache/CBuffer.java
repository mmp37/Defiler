package dblockcache;

import java.io.IOException;

import virtualdisk.CVirtualDisk;
import common.Constants.DiskOperationType;

public class CBuffer extends DBuffer{
	private boolean isHeld = false;
	private boolean isPinned = false;	// IO is in progress
	private boolean isClean = true;		// DFS has not released reference to dbuf
	private boolean isValid = true;
	private byte[] _buffer;

	private int _blockID;
	private CBufferCache _cache;
	private CVirtualDisk _disk;

	public CBuffer(int blockID, CBufferCache cache, CVirtualDisk disk){
		_blockID = blockID;
		_cache = cache;
		_disk = disk;
	}

	/* Start an asynchronous fetch of associated block from the volume */
	public void startFetch() {
		isPinned = true;
		// fetch
		try {
			_disk.startRequest(this, DiskOperationType.READ);
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
		isValid = true;
	}

	/* Start an asynchronous write of buffer contents to block on volume */
	public void startPush(){
		isPinned = true;
		try {
			_disk.startRequest(this, DiskOperationType.WRITE);
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}

	}
	
	public void setIsHeld(boolean held){
		isHeld = held;
	}

	/* Check whether the buffer has valid data */ 
	public boolean checkValid(){
		return isValid;
	}

	/* Wait until the buffer has valid data, i.e., wait for fetch to complete */
	public boolean waitValid(){
		while(!this.checkValid()){
			this.startFetch();
		}
		return isValid;
	}

	/* Check whether the buffer is dirty, i.e., has modified data written back to disk? */
	public boolean checkClean(){
		return isClean;
	}

	/* Wait until the buffer is clean, i.e., wait until a push operation completes */
	public boolean waitClean(){
		while(!this.checkClean()){
			this.startPush();
		}
		return isClean;
	}

	/* Check if buffer is evictable: not evictable if I/O in progress, or buffer is held */
	public boolean isBusy(){
		return (isHeld || isPinned);
	}

	/*
	 * reads into the buffer[] array from the contents of the DBuffer. Check
	 * first that the DBuffer has a valid copy of the data! startOffset and
	 * count are for the buffer array, not the DBuffer. Upon an error, it should
	 * return -1, otherwise return number of bytes read.
	 */
	public int read(byte[] buffer, int startOffset, int count){
		this.waitValid();
		int numRead = 0;
		for(int i = 0; i < count; i++){
			if(startOffset+i < buffer.length && i<_buffer.length){
				buffer[startOffset+i] = _buffer[i];
				numRead++;
			}
		}
		return numRead;
	}

	/*
	 * writes into the DBuffer from the contents of buffer[] array. startOffset
	 * and count are for the buffer array, not the DBuffer. Mark buffer dirty!
	 * Upon an error, it should return -1, otherwise return number of bytes
	 * written.
	 */
	public int write(byte[] buffer, int startOffset, int count){
		int numWritten = 0;
		_buffer = new byte[count];
		for(int i = 0; i < count; i++){
			if(startOffset+i < buffer.length){
				_buffer[i] = buffer[startOffset+i];
				numWritten++;
			}
		}		
		isClean = false;
		return numWritten;
	}

	/* An upcall from VirtualDisk layer to inform the completion of an IO operation */
	public void ioComplete(){
		isPinned = false;
	}

	/* An upcall from VirtualDisk layer to fetch the blockID associated with a startRequest operation */
	public int getBlockID(){
		return _blockID;
	}

	/* An upcall from VirtualDisk layer to fetch the buffer associated with DBuffer object*/
	public byte[] getBuffer(){
		return _buffer;
	}
}
