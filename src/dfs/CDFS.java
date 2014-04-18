package dfs;

import java.util.List;
import java.util.ArrayList;
import common.Constants;
import common.DFileID;

import dblockcache.CBufferCache;
import dblockcache.CBuffer;

public class CDFS extends DFS{
	
private boolean _format;
private String _volName;
private ArrayList<DFileID> fileIDs;
private DFileID deletedFile;
private int numBlocks; // 2^18
private int blockSize; // 1kB
private int inodeSize; //32 Bytes
private int numCacheBlocks; // 2^16
private int maxFileSize; // Constraint on the max file size

private int maxDFiles; // For recycling DFileIDs

private CBufferCache _cache;

/* 
 * @volName: Explicitly overwrite volume name
 * @format: If format is true, the system should earse the underlying disk contents and reinialize the volume.
 */

CDFS(String volName, boolean format) {
	_volName = volName;
	_format = format;
	deletedFile = new DFileID(-1);
	_cache = new CBufferCache(numCacheBlocks * Constants.BLOCK_SIZE ,this);
}

CDFS(boolean format) {
	this(Constants.vdiskName,format);
}

CDFS() {
	this(Constants.vdiskName,false);
}

/* Initialize all the necessary structures with sizes as specified in the common/Constants.java */
public void init(int numBlock, int maxBlockSize, int maxInodeSize, int numCacheBlock, int maxNumDFiles) {
	numBlocks = numBlock;
	blockSize = maxBlockSize;
	inodeSize = maxInodeSize;
	numCacheBlocks = numCacheBlock;
	maxDFiles = maxNumDFiles;
	maxFileSize = blockSize*500;
	fileIDs = new ArrayList<DFileID>();
}

/* creates a new DFile and returns the DFileID, which is useful to uniquely identify the DFile*/
public DFileID createDFile() {
	int i;
	DFileID newFile;
	for(i = 0; i <fileIDs.size(); i++) {
		if(fileIDs.get(i).equals(deletedFile)) {
			newFile = new DFileID(i);
			fileIDs.set(i, newFile);
			return newFile;
		}
	}
	i++;
	newFile = new DFileID(i);
	fileIDs.add(newFile);
	return newFile;
}

/* destroys the file specified by the DFileID */
public void destroyDFile(DFileID dFID){
	fileIDs.set(dFID.getDFileID(), deletedFile);
	//need to mark blocks/inode on disk deleted
}

/*
 * reads the file dfile named by DFileID into the buffer starting from the
 * buffer offset startOffset; at most count bytes are transferred
 */
public int read(DFileID dFID, byte[] buffer, int startOffset, int count) {
	int countRem = count;
	int blockNum = 0;
	while(countRem > 0) {
		CBuffer transaction = new CBuffer();
		transaction.setFileID(dFID);
		transaction.setBlockID(blockNum);
		transaction.read(buffer, startOffset + (blockNum * blockSize), count);
		countRem-=blockSize;
		blockNum++;
	}
	return count;
}

/*
 * writes to the file specified by DFileID from the buffer starting from the
 * buffer offset startOffset; at most count bytes are transferred
 */
public int write(DFileID dFID, byte[] buffer, int startOffset, int count) {
	int countRem = count;
	int blockNum = 0;
	while(countRem > 0) {
		CBuffer transaction = new CBuffer();
		transaction.setFileID(dFID);
		transaction.setBlockID(blockNum);
		transaction.write(buffer, startOffset, count);
		countRem-=blockSize;
		blockNum++;
	}
	return count;
}

/* returns the size in bytes of the file indicated by DFileID. */
public int sizeDFile(DFileID dFID) {
	
}

/* 
 * List all the existing DFileIDs in the volume
 */
public ArrayList<DFileID> listAllDFiles() {
	return _fileIDs;
}

/* Write back all dirty blocks to the volume, and wait for completion. */
public void sync(){
	
}
}
