package dfs;

import java.util.List;
import java.util.ArrayList;
import common.Constants;
import common.DFileID;

public class CDFS extends DFS{
	
private boolean _format;
private String _volName;
private ArrayList<DFileID> fileIDs;
private int numBlocks; // 2^18
private int blockSize; // 1kB
private int inodeSize; //32 Bytes
private int numCacheBlocks; // 2^16
private int maxFileSize; // Constraint on the max file size

private int maxDFiles; // For recylcing DFileIDs

/* 
 * @volName: Explicitly overwrite volume name
 * @format: If format is true, the system should earse the underlying disk contents and reinialize the volume.
 */

CDFS(String volName, boolean format) {
	_volName = volName;
	_format = format;
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
	
}

/* destroys the file specified by the DFileID */
public void destroyDFile(DFileID dFID){
	
}

/*
 * reads the file dfile named by DFileID into the buffer starting from the
 * buffer offset startOffset; at most count bytes are transferred
 */
public int read(DFileID dFID, byte[] buffer, int startOffset, int count) {
	
}

/*
 * writes to the file specified by DFileID from the buffer starting from the
 * buffer offset startOffset; at most count bytes are transferred
 */
public int write(DFileID dFID, byte[] buffer, int startOffset, int count) {
	
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
