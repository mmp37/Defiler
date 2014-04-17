package dblockcache;

import common.Constants;

public class CBufferCache {
	
	private int _cacheSize;
	
	/*
	 * Constructor: allocates a cacheSize number of cache blocks, each
	 * containing BLOCK-size bytes data, in memory
	 */
	public CBufferCache(int cacheSize) {
		_cacheSize = cacheSize * Constants.BLOCK_SIZE;
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
