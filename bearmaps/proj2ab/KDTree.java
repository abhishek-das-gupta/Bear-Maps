package bearmaps.proj2ab;

import java.util.List;

public class KDTree implements PointSet {
    private Node root;

    public KDTree(List<Point> points){
        for(Point pt : points){
            root = insert(pt,root,Node.horizontal);
        }

    }

    private Node insert(Point pt,Node node,boolean orientation){
        if(node == null){
            return new Node(pt,orientation);
        }
        if(pt.equals(node.getPoint())){
            return node;
        }
        int cmp = compare(pt, node,orientation);
        if(cmp < 0){
            node.left = insert(pt,node.left,!orientation);
        }else if(cmp > 0){
            node.right = insert(pt,node.right,!orientation);
        }else if(cmp == 0){
            node.right = insert(pt,node.right,!orientation);
        }
        return node;
    }

    private int compare(Point pt, Node node, boolean orientation){
        if(orientation == Node.horizontal){
            return Double.compare(pt.getX(), node.getX());
        }else{
            return Double.compare(pt.getY(), node.getY());
        }
    }

    private int compare(Point pt, Node n){
        if(n.orientation == Node.horizontal){
            return Double.compare(pt.getX(),n.getPoint().getX());
        }else{
            return Double.compare(pt.getY(),n.getPoint().getY());
        }

    }

    @Override
    public Point nearest(double x, double y) {
        Point goal = new Point(x,y);
        Point best = nearest(root,  goal, root.getPoint());
        return best;
    }

    private Point nearest(Node node,Point goal,Point best){
        if(node == null){
            return best;
        }
        if(Point.distance(goal,node.getPoint()) < Point.distance(goal,best)){
            best = node.getPoint();
        }

        int cmp = compare(goal,node);
        Node goodChild, badChild;
        if(cmp > 0){
            goodChild = node.right;
            badChild = node.left;
        }else{
            goodChild = node.left;
            badChild = node.right;
        }

        best = nearest(goodChild,goal,best);
        if(isWorthLooking(node,goal,best)){
            best = nearest(badChild,goal,best);
        }
        return best;

    }

    private boolean isWorthLooking(Node node, Point goal, Point best){
        double distToBest = Point.distance(best,goal);
        double distToBad;
        if(node.orientation == Node.horizontal){
            distToBad = /*Math.pow(node.getPoint().getY() - goal.getY(),2);*/Point.distance(new Point(goal.getX(),node.getPoint().getY()),goal);
        }else{
            distToBad = /*Math.pow(node.getPoint().getX() - goal.getX(),2);*/Point.distance(new Point(node.getPoint().getX(), goal.getY()), goal);
        }
        return distToBad < distToBest;
    }

    public static KDTree buildLectureTree(){
        Point p1 = new Point(0.757,0.377);
        Point p2 = new Point(0.548,0.866);
        Point p3 = new Point(0.534,0.463);
        Point p4 = new Point(0.830,0.477);
        Point p5 = new Point(0.475,0.191);
        Point p6 = new Point(0.6088,0.542);
        Point p7 = new Point(0.909,0.049);
        Point p8 = new Point(0.778,0.149);
        Point p9 = new Point(0.483,0.144);
        Point p10 = new Point(0.189,0.741);

        KDTree kd = new KDTree(List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10));
        return kd;

    }


    private class Node{
        public static final boolean horizontal = false;
        public static final boolean vertical = true;
        private Point point;
        Node left;
        Node right;
        boolean orientation;

        public Node(Point pt,boolean orientation){
            point = pt;
            left = null;
            right = null;
            this.orientation = orientation;
        }

        public double getX(){
            return point.getX();
        }

        public double getY(){
            return point.getY();
        }

        public Point getPoint(){
            return point;
        }


    }

//    public static void main(String[] args){
//        KDTree kd = KDTree.buildLectureTree();
//        System.out.println(kd.nearest(0.723,0.0887));
//    }
}
