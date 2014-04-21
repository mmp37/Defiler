package virtualdisk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.ArrayList;

import common.Constants;
import common.Constants.DiskOperationType;

import dblockcache.CBuffer;
import dblockcache.CBufferCache;
import dblockcache.DBuffer;

public class CVirtualDisk extends VirtualDisk{
	
	private CBufferCache _cache;

	/*
	 * VirtualDisk Constructors
	 */
	public CVirtualDisk(String volName, boolean format, CBufferCache cache) throws FileNotFoundException,
			IOException {

		_volName = volName;
		_maxVolSize = Constants.BLOCK_SIZE * Constants.NUM_OF_BLOCKS;
		_cache = cache;

		/*
		 * mode: rws => Open for reading and writing, as with "rw", and also
		 * require that every update to the file's content or metadata be
		 * written synchronously to the underlying storage device.
		 */
		_file = new RandomAccessFile(_volName, "rws");

		/*
		 * Set the length of the file to be NUM_OF_BLOCKS with each block of
		 * size BLOCK_SIZE. setLength internally invokes ftruncate(2) syscall to
		 * set the length.
		 */
		_file.setLength(Constants.BLOCK_SIZE * Constants.NUM_OF_BLOCKS);
		
		if(format) {
			this.formatStore();
			this.initFormat();
		}
		else {
			this.initPreExisting();
		}
		/* Other methods as required */
		// Setup INODE region.
	}

	
	private void initFormat() throws IOException {		
		//from block (((maxDfiles*inodesize)/Blocksize) + 1) to the end of the file, do nothing
		//from block 1 - block ((maxDfiles*(inodesize +  4))/Blocksize), map memory space to inodes
		int currBlock = (int)Math.ceil((Constants.MAX_DFILES * (Constants.INODE_SIZE + 4))/Constants.BLOCK_SIZE) + 1;
		int filePtr;
		for(int i = 0; i < Constants.MAX_DFILES; i++) {
			filePtr = Constants.BLOCK_SIZE + (i* (Constants.INODE_SIZE+4)) + 4;
			for(int j = 0; j < Constants.INODE_SIZE; j++) {
				_file.seek(filePtr);
				_file.write(currBlock);
				filePtr++;
				currBlock++;
			}
		}
	}
	
	private List<Integer> initPreExisting() throws IOException {
		ArrayList<Integer> files = new ArrayList<Integer>();
		int filePtr;
		int inUse;
		for(int i = 0; i < Constants.MAX_DFILES; i++) {
			filePtr = Constants.BLOCK_SIZE + (i* (Constants.INODE_SIZE+4));
			_file.seek(filePtr);
			inUse = _file.readInt();
			if(1 == inUse) {
				files.add(i+1, i+1);
			}
		}
		
		return files;//this info needs to get passed up to the CDFS on a non-formatted start
	}


	/*
	 * Start an asynchronous request to the underlying device/disk/volume. 
	 * -- buf is an DBuffer object that needs to be read/write from/to the volume.	
	 * -- operation is either READ or WRITE  
	 */
	public void startRequest(DBuffer buf, DiskOperationType operation) throws IllegalArgumentException,
			IOException {
		if(operation == DiskOperationType.READ) {
			this.readBlock(buf);
		}
		else if(operation == DiskOperationType.WRITE) {
			this.writeBlock(buf);
		}
		buf.ioComplete();
	}



}
