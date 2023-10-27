package lab.parser;

public class TreeNode {
	/*
	 * Сделать приватным и гетеры/сетеры
	 */
	public String data;
	public TreeNode left;
	public TreeNode right;
	
	public TreeNode(String data) {
		this.data = data;
		this.left = null;
		this.right = null;
	}
}
