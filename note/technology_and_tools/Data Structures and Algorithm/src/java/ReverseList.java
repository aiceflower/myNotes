import util.ListNode;

public class ReverseList{
    /**
     * 题目描述：反转链表
     * 1->2->3->4->5
     * 5->4->3->2->1
     * @param args
     */
    public static void main(String[] args){
        ListNode n1 = new ListNode(1, null);
        ListNode n2 = new ListNode(2, n1);
        ListNode n3 = new ListNode(3, n2);
        ListNode n4 = new ListNode(4, n3);
        ListNode n5 = new ListNode(5, n4);
        //ListNode r = reverse(n5);
        ListNode d = digui(n5);
        System.out.println("end.");
    }
    
    //迭代
    public static ListNode reverse(ListNode node){
        ListNode cur = node;
        ListNode next = null, pre = null;
        while(cur != null){
            next = cur.next; 
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    //递归
    public static ListNode digui(ListNode node){
        if (node == null || node.next == null){
            return node;
        }
        ListNode ln = digui(node.next);
        node.next.next = node;
        node.next = null;
        return ln;
    }


    
}