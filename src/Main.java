import common.Constants;

import dfs.CDFS;


public class Main {
	
	public static void main(String[] args){
		// check args for volName (String) and format (boolean)...
		int count = 1001;
		int bs = 50;
		int addOne = (count%bs == 0) ? 0 : 1;
		int numBlocks = (count / bs) + addOne;
		System.out.println(numBlocks);
		//CDFS myDFS = new CDFS();
	}

}
