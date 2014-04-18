package dfs;

import java.util.List;
import java.util.ArrayList;
import common.Constants;
import common.DFileID;

import dblockcache.CBufferCache;
import dblockcache.CBuffer;

public class CDFS extends DFS{
	
	private ArrayList<DFileID> fileIDs;
	private List<Integer> freedIDs;
	private int countID = 0;

	private CBufferCache _cache;

	/* 
	 * @volName: Explicitly overwrite volume name
	 * @format: If format is true, the system should erase the underlying disk contents and reinitialize the volume.
	 */

	public CDFS(String volName, boolean format) {
		_cache = new CBufferCache(this, volName, format);
		fileIDs = new ArrayList<DFileID>();
		freedIDs = new ArrayList<Integer>();
	}

	public CDFS(boolean format) {
		this(Constants.vdiskName,format);
	}

	public CDFS() {
		this(Constants.vdiskName,false);
	}
	
	@Override
	public void init() {
		fileIDs = new ArrayList<DFileID>();
	}

	/* creates a new DFile and returns the DFileID, which is useful to uniquely identify the DFile*/
	public DFileID createDFile() {
		int newID;
		if(countID == Constants.MAX_DFILES)
			newID = freedIDs.remove(0);
		else
			newID = countID++;
		DFileID newFile = new DFileID(newID);
		fileIDs.add(newFile);
		return newFile;
	}

	/* destroys the file specified by the DFileID */
	public void destroyDFile(DFileID dFID){
		freedIDs.add(dFID.getDFileID());
	}

	/*
	 * reads the file dfile named by DFileID into the buffer starting from the
	 * buffer offset startOffset; at most count bytes are transferred
	 */
	public int read(DFileID dFID, byte[] buffer, int startOffset, int count) {
		
		int addOne = (count%Constants.BLOCK_SIZE == 0) ? 0 : 1;
		int numBlocks = (count / Constants.BLOCK_SIZE) + addOne;
		return 0;
		
//		CBuffer transaction = new CBuffer();
//		transaction.setFileID(dFID);
//		return transaction.read(buffer, startOffset, count);
	}

	/*
	 * writes to the file specified by DFileID from the buffer starting from the
	 * buffer offset startOffset; at most count bytes are transferred
	 */
	public int write(DFileID dFID, byte[] buffer, int startOffset, int count) {
		
		int addOne = (count%Constants.BLOCK_SIZE == 0) ? 0 : 1;
		int numBlocks = (count / Constants.BLOCK_SIZE) + addOne;
		return 0;
		
//		CBuffer transaction = new CBuffer();
//		transaction.setFileID(dFID);
//		return transaction.write(buffer, startOffset, count);
	}

	/* returns the size in bytes of the file indicated by DFileID. */
	public int sizeDFile(DFileID dFID) {
		return 0;
		
	}

	/* 
	 * List all the existing DFileIDs in the volume
	 */
	public ArrayList<DFileID> listAllDFiles() {
		return fileIDs;
	}

	/* Write back all dirty blocks to the volume, and wait for completion. */
	public void sync(){
		_cache.sync();
	}

}
