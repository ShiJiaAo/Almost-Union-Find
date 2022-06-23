/**
 * Name: Shi Jiaao
 * Student ID: XXXXXXXXX
 */

import java.io.*;
import java.util.*;

public class almostUF {
    public static class Parent {
        public int size;
        public long sum;
        public Parent Parent;
        public int rank;

        public Parent(int size, long sum, int rank) {
            this.size = size;
            this.sum = sum;
            this.rank = rank;
            this.Parent = null;
        }

        public Parent findParent() {
            if (this.Parent == null) {
                return this;
            }

            // path-compression heuristic
            this.Parent = findParent(this.Parent);
            return this.Parent;
        }

        public Parent findParent(Parent find) {
            if (find.Parent == null) {
                return find;
            }
            find.Parent = findParent(find.Parent);
            return find.Parent;
        }
    }

    public static void move(int toMove, int target, Parent[] parentList) {
        if (parentList[toMove].findParent() == parentList[target].findParent()) {
            return;
        }

        // decrementing size and sum
        parentList[toMove].findParent().size -= 1;
        parentList[toMove].findParent().sum -= toMove;

        // moving
        parentList[toMove] = parentList[target].findParent();

        // incrementing size and sum
        parentList[target].findParent().size += 1;
        parentList[target].findParent().sum += toMove;
    }

    public static void union(int set1, int set2, Parent[] parentList) {
        Parent ParentSet1 = parentList[set1].findParent();
        Parent ParentSet2 = parentList[set2].findParent();
        if (ParentSet1 == ParentSet2) {
            // same set
            return;
        }

        // union-by-rank heuristic
        if (ParentSet1.rank > ParentSet2.rank) {
            ParentSet2.Parent = ParentSet1;
            ParentSet1.size += ParentSet2.size;
            ParentSet1.sum += ParentSet2.sum;
        }
        if (ParentSet2.rank > ParentSet1.rank) {
            ParentSet1.Parent = ParentSet2;
            ParentSet2.size += ParentSet1.size;
            ParentSet2.sum += ParentSet1.sum;
        }
        if (ParentSet2.rank == ParentSet1.rank) {
            Parent newParent = new Parent(ParentSet1.size + ParentSet2.size, ParentSet1.sum + ParentSet2.sum,
                    ParentSet1.rank + 1);
            ParentSet1.Parent = newParent;
            ParentSet2.Parent = newParent;
        }
    }

    public static void query(int target, PrintWriter pw, Parent[] parentList) throws IOException {
        Parent reference = parentList[target].findParent();
        pw.print(reference.size + " " + reference.sum);
        pw.println("");
    }

    /* reads and executes command */
    public static void readCommand(BufferedReader br, PrintWriter pw, Parent[] parentList) throws IOException {
        String helper = br.readLine();
        String command[] = helper.split(" ");
        int commandValue = Integer.parseInt(command[0]);

        if (commandValue == 1) {
            int p = Integer.parseInt(command[1]);
            int q = Integer.parseInt(command[2]);
            union(p, q, parentList);
        }

        if (commandValue == 2) {
            int p = Integer.parseInt(command[1]);
            int q = Integer.parseInt(command[2]);
            move(p, q, parentList);
        }

        if (commandValue == 3) {
            int p = Integer.parseInt(command[1]);
            query(p, pw, parentList);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        String helper;
        do {
            helper = br.readLine();
            if (helper == null) {
                break;
            }
            String[] testCase = helper.split(" ");
            int n = Integer.parseInt(testCase[0]);
            int m = Integer.parseInt(testCase[1]);

            // initialising the sets 
            Parent[] sets = new Parent[n + 1];
            sets[0] = null;
            for (int i = 1; i <= n; i++) {
                sets[i] = new Parent(1, i, 0);
            }

            // reading and executing the commands
            for (int i = 0; i < m; i++) {
                readCommand(br, pw, sets);
            }
        } while (helper != null);

        br.close();
        pw.close();
    }
}