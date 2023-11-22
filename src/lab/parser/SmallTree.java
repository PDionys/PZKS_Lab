package lab.parser;
import lab.parser.TreeNode;

public class SmallTree {
	private String value;
	private String key;
	
	private int prior;
	private TreeNode node = null;
	
	SmallTree(String value, String key, int prior){
		this.value = value;
		this.key = key;
		this.prior = prior;
	}
	
	SmallTree(TreeNode node, String key){
		this.value = null;
		this.key = key;
		this.prior = -1;
		
		this.node = node;
	}
	
	SmallTree(TreeNode node, String key, int prior){
		this.value = null;
		this.key = key;
		this.prior = prior;
		
		this.node = node;
	}
	
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getKey() {
		return this.key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public int getPrior() {
		return this.prior;
	}
	
	public TreeNode getNode() {
		return this.node;
	}
	public void setNode(TreeNode node) {
		this.node = node;
	}
	
	public String toString() {
		return this.value + ":[" + this.key + "]" + this.prior + ", ";
	}
}
