package dfs;

import java.util.List;
import java.util.ArrayList;

import virtualdisk.CVirtualDisk;
import common.Constants;
import common.DFileID;

import dblockcache.CBufferCache;
import dblockcache.CBuffer;

public class CDFS extends DFS{

	private ArrayList<DFileID> fileIDs;
	private List<Integer> freedIDs;
	private int countID = 0;
	private String _volName;
	private boolean _format;

	private CBufferCache _cache;

	/* 
	 * @volName: Explicitly overwrite volume name
	 * @format: If format is true, the system should erase the underlying disk contents and reinitialize the volume.
	 */

	public CDFS(String volName, boolean format) {
		_volName = volName;
		_format = format;
		_cache = new CBufferCache(this, _volName, _format);
		freedIDs = new ArrayList<Integer>();
		this.init();
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
		if(_format){
			CVirtualDisk disk = _cache.getDisk();
			//fileIDs = disk.getFilesOnInit();
		}
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

//		int addOne = ((count+startOffset)%Constants.BLOCK_SIZE == 0) ? 0 : 1;
//		int numBlocks = ((count+startOffset) / Constants.BLOCK_SIZE) + addOne;
		int blockID = dFID.getDFileID()*Constants.MAX_FILE_SIZE;
		int totalRead = 0;

		while(count!=0){
			int toRead = Constants.BLOCK_SIZE - startOffset;
			if(toRead > count)
				toRead = count;
			
			CBuffer cbuf = (CBuffer) _cache.getBlock(blockID++);
			int amountRead = cbuf.read(buffer, startOffset, toRead);
			
			if(amountRead == -1){
				System.out.println("Error occured while reading, breaking loop");
				break;
			}
			
			totalRead += amountRead;
			count -= amountRead;
			startOffset = 0;
		}
		return totalRead;
	}

	/*
	 * writes to the file specified by DFileID from the buffer starting from the
	 * buffer offset startOffset; at most count bytes are transferred
	 */
	public int write(DFileID dFID, byte[] buffer, int startOffset, int count) {

		int blockID = dFID.getDFileID()*Constants.MAX_FILE_SIZE;
		int totalWritten = 0;

		while(count!=0){
			int toWrite = Constants.BLOCK_SIZE - startOffset;
			if(toWrite > count)
				toWrite = count;
			
			CBuffer cbuf = (CBuffer) _cache.getBlock(blockID++);
			int amountWritten = cbuf.write(buffer, startOffset, toWrite);
			
			if(amountWritten == -1){
				System.out.println("Error occured while reading, breaking loop");
				break;
			}
			
			totalWritten += amountWritten;
			count -= amountWritten;
			startOffset = 0;
		}
		return totalWritten;
	}

	/* returns the size in bytes of the file indicated by DFileID. */
	public int sizeDFile(DFileID dFID) {
		if(fileIDs.contains(dFID)){
			int index = fileIDs.indexOf(dFID);
			return fileIDs.get(index).getSize();
		}
		return -1;
	}

	/* 
	 * List all the existing DFileIDs in the volume
	 */
	public List<DFileID> listAllDFiles() {
		return fileIDs;
	}

	/* Write back all dirty blocks to the volume, and wait for completion. */
	public void sync(){
		_cache.sync();
	}

}
