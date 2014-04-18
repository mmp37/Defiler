package dblockcache;

import virtualdisk.CVirtualDisk;
import common.Constants;
import dfs.CDFS;

public class CBufferCache {
	
	private int _cacheSize;
	private CVirtualDisk _disk;
	private CDFS _dfs;
	
	/*
	 * Constructor: allocates a cacheSize number of cache blocks, each
	 * containing BLOCK-size bytes data, in memory
	 */
	public CBufferCache(int cacheSize, CDFS dfs) {
		_cacheSize = cacheSize * Constants.BLOCK_SIZE;
		_dfs = dfs;
		_disk = new CVirtualDisk(this);
	}
	
	/*
	 * Get buffer for block specified by blockID. The buffer is "held" until the
	 * caller releases it. A "held" buffer cannot be evicted: its block ID
	 * cannot change.
	 */
	public CBuffer getBlock(int blockID) {
		
	}

	/* Release the buffer so that others waiting on it can use it */
	public void releaseBlock(CBuffer buf){
		
	}
	
	/*
	 * sync() writes back all dirty blocks to the volume and wait for completion.
	 * The sync() method should maintain clean block copies in DBufferCache.
	 */
	public void sync(){
		
	}
}
