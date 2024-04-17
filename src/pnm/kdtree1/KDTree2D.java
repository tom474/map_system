package pnm.kdtree1;

public class KDTree2D {
    private KDNode root = null;

    /**
     * Adds a new point to the KDTree. This method starts the recursive insertion
     * by comparing the x-coordinate first (vertical splitting).
     *
     * @param x the x-coordinate of the new point to add
     * @param y the y-coordinate of the new point to add
     */
    public void add(int x, int y) {
        root = add(root, x, y, true);
    }

    /**
     * Recursively adds a new point to the KDTree at the correct location based on
     * the dimension (x or y) comparison. The method alternates between comparing
     * x and y coordinates based on the depth of the tree (even levels compare x,
     * odd levels compare y).
     *
     * @param node the current node in the KDTree during the recursion
     * @param x the x-coordinate of the new point to add
     * @param y the y-coordinate of the new point to add
     * @param vertical a boolean indicating if the current level of the tree
     *                 should compare x-coordinates (true) or y-coordinates (false)
     * @return the current node after potentially updating its child references
     */
    private KDNode add(KDNode node, int x, int y, boolean vertical) {
        // Base case: If the spot is found to be empty, insert a new node here.
        if (node == null) return new KDNode(x, y);

        // Recursive case: Decide whether to go left or right in the tree
        // and toggle the dimension by using !vertical for the next depth level.
        if (vertical) {
            // Compare x-coordinates if the current level is vertical.
            if (x < node.x) {
                node.left = add(node.left, x, y, !vertical); // Go left for less than
            } else {
                node.right = add(node.right, x, y, !vertical); // Go right for greater or equal
            }
        } else {
            // Compare y-coordinates if the current level is not vertical.
            if (y < node.y) {
                node.left = add(node.left, x, y, !vertical); // Go left for less than
            } else {
                node.right = add(node.right, x, y, !vertical); // Go right for greater or equal
            }
        }
        return node; // Return the node itself after the recursive call.
    }

    /**
     * Initiates the deletion of a point from the KDTree. This method starts the recursive deletion
     * process by considering the x-coordinate first (vertical splitting).
     *
     * @param x the x-coordinate of the point to delete
     * @param y the y-coordinate of the point to delete
     * @throws RuntimeException if the point is not found in the KDTree
     */
    public void delete(int x, int y) {
        root = delete(root, x, y, true);
    }

    /**
     * Recursively deletes a point from the KDTree. If the point is found, it handles three cases:
     * 1) Node with two children: Replace this node with the minimum node from the right subtree
     *    or the left subtree if the right is empty.
     * 2) Node with one child: Replace the node with its only child.
     * 3) Leaf node: Simply remove the leaf.
     * This method alternates between x and y dimensions based on the tree level.
     *
     * @param node the current node in the KDTree during recursion
     * @param x the x-coordinate of the point to delete
     * @param y the y-coordinate of the point to delete
     * @param vertical a boolean indicating if the current level of the tree
     *                 should compare x-coordinates (true) or y-coordinates (false)
     * @return the modified subtree with the node deleted, or null if the node was a leaf
     * @throws RuntimeException if the point is not found in the subtree
     */
    private KDNode delete(KDNode node, int x, int y, boolean vertical) {
        if (node == null) throw new RuntimeException("Point not found!");

        // Check if the current node is the one to be deleted
        if (node.isEqual(x, y)) {
            // Handle the case where the node has two children
            if (node.right != null) {
                // Find the minimum node in the right subtree
                KDNode min = findMin(node.right, vertical);
                node.x = min.x;
                node.y = min.y;
                // Recursively delete the min node
                node.right = delete(node.right, min.x, min.y, !vertical);
            } else if (node.left != null) {
                // Handle the case where the node only has a left child
                KDNode min = findMin(node.left, vertical);
                node.x = min.x;
                node.y = min.y;
                node.right = delete(node.left, min.x, min.y, !vertical);
                node.left = null;
            } else {
                // Handle the case where the node is a leaf
                return null;
            }
        } else {
            // Recur down the tree to find the node to delete
            if (vertical) {
                if (x < node.x) node.left = delete(node.left, x, y, !vertical);
                else node.right = delete(node.right, x, y, !vertical);
            } else {
                if (y < node.y) node.left = delete(node.left, x, y, !vertical);
                else node.right = delete(node.right, x, y, !vertical);
            }
        }
        return node;  // Return the current node after potential modifications
    }

    /**
     * Finds the minimum node in a subtree of the KDTree based on a specified dimension (x or y).
     * This function searches recursively, considering the tree's dimension alternation. It returns
     * the node with the smallest value in the current dimension.
     *
     * @param node the current node from which to find the minimum in its subtree
     * @param vertical a boolean indicating if the current dimension to compare is x (true) or y (false)
     * @return the node with the minimum value in the specified dimension; null if the subtree is empty
     */
    private KDNode findMin(KDNode node, boolean vertical) {
        if (node == null) return null;  // Base case: return null if subtree is empty

        // Assume the current node is the minimum
        KDNode min = node;

        // Recursively find the minimum in the left subtree, toggling the dimension
        KDNode lmin = findMin(node.left, !vertical);
        // Recursively find the minimum in the right subtree, toggling the dimension
        KDNode rmin = findMin(node.right, !vertical);

        // Compare and update the minimum node based on the current dimension
        if (vertical) {
            // If we are comparing x-values
            if (lmin != null && lmin.x < min.x) min = lmin;  // Update min if left child has smaller x
            if (rmin != null && rmin.x < min.x) min = rmin;  // Update min if right child has smaller x
        } else {
            // If we are comparing y-values
            if (lmin != null && lmin.y < min.y) min = lmin;  // Update min if left child has smaller y
            if (rmin != null && rmin.y < min.y) min = rmin;  // Update min if right child has smaller y
        }

        return min;  // Return the minimum node found
    }

    /**
     * Finds the k closest points in the KDTree to a specified target point.
     * This method initializes the search process and collects the results from a max-heap.
     *
     * @param targetX the x-coordinate of the target point
     * @param targetY the y-coordinate of the target point
     * @param k the number of closest points to find
     * @return a 2D array containing the k closest points, each represented as an int array with two elements (x and y coordinates)
     */
    public int[][] findKClosestPoints(int targetX, int targetY, int k) {
        MaxHeap pq = new MaxHeap(k, targetX, targetY);  // Create a max-heap to store the closest points
        findKClosestPoints(root, targetX, targetY, k, true, pq);  // Start the recursive search

        SimpleList result = new SimpleList(k);  // Initialize a simple list to store the final results
        while (!pq.isEmpty()) {
            result.add(pq.poll());  // Retrieve points from the heap and add them to the result list
        }
        return result.toArray();  // Convert the list to an array and return
    }

    /**
     * Recursively searches for the closest points to a given target within the KDTree.
     * This method checks both the current node and decides which branch of the tree to explore next,
     * prioritizing branches closer to the target to potentially exclude distant branches.
     *
     * @param node the current node in the KDTree during the recursion
     * @param targetX the x-coordinate of the target point
     * @param targetY the y-coordinate of the target point
     * @param k the number of closest points to find
     * @param vertical a boolean indicating if the current level of the tree
     *                 should compare x-coordinates (true) or y-coordinates (false)
     * @param pq a max-heap used to store the closest points found so far
     */
    private void findKClosestPoints(KDNode node, int targetX, int targetY, int k, boolean vertical, MaxHeap pq) {
        if (node == null) return;  // Base case: if node is null, return immediately

        int[] point = new int[]{node.x, node.y};
        pq.offer(point);  // Offer the current node's point to the max-heap

        // Determine which branch is closer to the target point
        KDNode nextBranch = vertical ? (targetX < node.x ? node.left : node.right) : (targetY < node.y ? node.left : node.right);
        KDNode otherBranch = vertical ? (targetX < node.x ? node.right : node.left) : (targetY < node.y ? node.right : node.left);

        // Recursively explore the closer branch first to optimize the search
        findKClosestPoints(nextBranch, targetX, targetY, k, !vertical, pq);

        // Calculate the squared distance to the decision boundary
        long distToLine = vertical ? Math.abs(targetX - node.x) : Math.abs(targetY - node.y);
        // Explore the other branch only if necessary (if it might contain closer points)
        if (pq.size() < k || distToLine * distToLine < pq.distSquared(pq.peek()[0], pq.peek()[1])) {
            findKClosestPoints(otherBranch, targetX, targetY, k, !vertical, pq);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleQueue queue = new SimpleQueue();
        if (root != null) queue.add(root);

        while (!queue.isEmpty()) {
            KDNode node = queue.poll();
            sb.append('(').append(node.x).append(", ").append(node.y).append("), ");
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }

        if (sb.length() > 2) sb.setLength(sb.length() - 2); // Removes the trailing ", "
        return sb.toString();
    }

    public static void main(String[] args) {
        KDTree2D tree = new KDTree2D();
        tree.add(50, 50);
        tree.add(80, 40);
        tree.add(10, 60);
        tree.add(51, 38);
        tree.add(48, 38);

        System.out.println("KD-Tree contents: " + tree);

        System.out.println("KD-Tree contents before deletion: " + tree);
        tree.delete(48, 38);
        System.out.println("KD-Tree contents after deletion of (48, 38): " + tree);

        int[][] closestPoints = tree.findKClosestPoints(40, 40, 3);
        System.out.println("3 closest points to (40, 40):");
        for (int[] point : closestPoints) {
            System.out.println("(" + point[0] + ", " + point[1] + ")");
        }
    }
}

class KDNode {
    KDNode left = null, right = null;
    int x, y;

    public KDNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    boolean isEqual(int x, int y) {
        return this.x == x && this.y == y;
    }
}
