
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Fill in the implementation details of the class DecisionTree using this file.
 * Any methods or secondary classes that you want are fine but we will only
 * interact with those methods in the DecisionTree framework.
 *
 * You must add code for the 5 methods specified below.
 *
 * See DecisionTree for a description of default methods.
 */
public class DecisionTreeImpl extends DecisionTree {

    private DecTreeNode root;
    private List<String> labels; // ordered list of class labels 0 1 2...
    private List<String> attributes; // ordered list of attributes
    private Map<String, List<String>> attributeValues; // map to ordered
    // discrete values taken
    // by attributes

    /**
     * Answers static questions about decision trees.
     */
    DecisionTreeImpl() {
        // no code necessary
        // this is void purposefully
    }

    /**
     * Build a decision tree given only a training set.
     *
     * @param train: the training set
     */
    DecisionTreeImpl(DataSet train) {

        this.labels = train.labels;
        this.attributes = train.attributes;
        this.attributeValues = train.attributeValues;
        // TODO: add code here
       buildTree(train.instances, train.attributes, null, null, train);

    }

    /**
     * Build a decision tree given a training set then prune it using a tuning
     * set.
     *
     * @param train: the training set
     * @param tune: the tuning set
     */
    DecisionTreeImpl(DataSet train, DataSet tune) {

        this.labels = train.labels;
        this.attributes = train.attributes;
        this.attributeValues = train.attributeValues;
        
        buildTree(train.instances, train.attributes, null, null, train);
        //after building loop through each interior node and set terminal to true then test infoGain from the change
        //then eventually when you find the best infoGain, keep that node's terminal set to true
 
    }

    private DecTreeNode buildTree(
            List<Instance> examples, List<String> attributes,
            List<Instance> parentExamples, DecTreeNode curr, DataSet train) {
        
        
        if(examples.isEmpty()){
           DecTreeNode leafNode = new DecTreeNode(1, bestAttribute(train), -1, false); //make root
        }
        if(allSame(examples)){
            DecTreeNode leafNode = new DecTreeNode(majority(examples), -1, curr.attribute, true); //make leaf
            return leafNode;
        }
        DecTreeNode prev = curr;
        int bestAttribute = bestAttributeList(examples,attributes);
        int sizeList = attributeValues.get(attributes.get(bestAttribute)).size();
            for (int j = 0; j < sizeList; j++) { //for all values of the attribute split into subtrees
                //examples become examples with attribute value of j
                 for (int k = 0; k < examples.size(); k++) { //for every instance
                        // System.out.println(train.instances.get(k).attributes.get(i));
                        if (examples.get(k).attributes.get(bestAttribute) == j) { 
                            examples.remove(k);
                        }
                 }
                 attributes.set(bestAttribute, "0"); //set to 0 to recognize later that it's been used
                //build new node with examples, attribtues - bestAttribute etc.
                 DecTreeNode nextNode = new DecTreeNode(majority(examples), j, prev.attribute, false);
                buildTree(examples, attributes, examples, nextNode, train);
            }
        //normal case find best attribute and split along values
        return null; //end (Won't actually return null maybe)
    }
    
    public boolean allSame(List<Instance> list){
        int ones = 0;
        int zeros = 0;
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).label == 0){
                zeros++;
            } else {
                ones++;
            }
        }
        if (zeros==0||ones==0){
            return true;
        } else{
            return false;
        }
    }
    
    public int majority(List<Instance> list){
        int ones = 0;
        int zeros = 0;
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).label == 0){
                zeros++;
            } else {
                ones++;
            }
        }
        if (zeros>=ones){
            return 0;
        } else{
            return 1;
        }
    }

    public int majorityLabel(DataSet train, int attribute) {
        int majority = 0;
        if (attribute == -1) {
            double zeros = 0;
            double ones = 0;
            double count = 0;
            double entropy;
            double value;
            for (int i = 0; i < train.instances.size(); i++) {
                value = train.instances.get(i).label;

                if (value == 0) {
                    zeros++;
                } else {
                    ones++;
                }
                count++;
            }
            if (zeros >= ones) {
                majority = 0;
            } else {
                majority = 1;
            }


        } else {
            double zeros = 0;
            double ones = 0;

            double value;
            int sizeList = attributeValues.get(attributes.get(attribute)).size();
            for (int j = 0; j < sizeList; j++) {
                for (int k = 0; k < train.instances.size(); k++) { //for every instance
                    // System.out.println(train.instances.get(k).attributes.get(i));
                    if (train.instances.get(k).attributes.get(attribute) == j) { //check instance if the attribute of index i is equal to j, a int value

                        if (train.instances.get(k).label == 0) {
                            zeros++;
                        }
                        if (train.instances.get(k).label == 1) {
                            ones++;
                        }
                    }
                    if (zeros >= ones) {
                        majority = 0;
                    } else {
                        majority = 1;
                    }


                }
            }
        }
        return majority;
    }

    @Override
    public String classify(Instance instance) {

        String value = "0";
        //step through tree using instance and see how it gets classified and then update value
        return value;
    }

    @Override
    /**
     * Print the decision tree in the specified format
     */
    public void print() {

        printTreeNode(root, null, 0);
    }

    /**
     * Prints the subtree of the node with each line prefixed by 4 * k spaces.
     */
    public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            sb.append("    ");
        }
        String value;
        if (parent == null) {
            value = "ROOT";
        } else {
            String parentAttribute = attributes.get(parent.attribute);
            value = attributeValues.get(parentAttribute).get(p.parentAttributeValue);
        }
        sb.append(value);
        if (p.terminal) {
            sb.append(" (" + labels.get(p.label) + ")");
            System.out.println(sb.toString());
        } else {
            sb.append(" {" + attributes.get(p.attribute) + "?}");
            System.out.println(sb.toString());
            for (DecTreeNode child : p.children) {
                printTreeNode(child, p, k + 1);
            }
        }
    }
// loop through attributes like in bestAttribute but just print the info gain
    //from each one instead of looking for best one

    //   @Override
    public void rootInfoGain(DataSet train) {


        this.labels = train.labels;
        this.attributes = train.attributes;
        this.attributeValues = train.attributeValues;
        List<Instance> inst = train.instances;
        double totalEntropy = 0;
        double infoGain = 0;
        // TODO: add code here
        int value;
        double count = 0;
        double zeros = 0;
        double ones = 0;
        double entropy = 0;
        int sizeList;
        double entropyTotalAttribute = 0;


        totalEntropy = entropyNoAttribute(train);

        for (int i = 0; i < attributes.size(); i++) { //for the attributes
            entropyTotalAttribute = 0;
            sizeList = attributeValues.get(attributes.get(i)).size();
            for (int j = 0; j < sizeList; j++) { //for all values of the attribute
//                         if (i==4){
//                            System.out.println(entropyTotalAttribute);
//                         }
                count = 0;
                zeros = 0;
                ones = 0;
                entropy = 0;
                for (int k = 0; k < train.instances.size(); k++) { //for every instance
                    // System.out.println(train.instances.get(k).attributes.get(i));
                    if (train.instances.get(k).attributes.get(i) == j) { //check instance if the attribute of index i is equal to j, a int value
                        count++;
                        if (train.instances.get(k).label == 0) {
                            zeros++;
                        }
                        if (train.instances.get(k).label == 1) {
                            ones++;
                        }
                    }

                }
                if (count == 0) {
                    entropy = 0;
                } else {
                    entropy = -((zeros / count) * ((Math.log(zeros / count)) / Math.log(2))) - ((ones / count) * ((Math.log(ones / count)) / Math.log(2)));
                    entropy = entropy * (count / train.instances.size());
                }
                entropyTotalAttribute = entropyTotalAttribute + entropy;
                //  System.out.println( "how many values does A5 have "  + attributeValues.get(attributes.get(4))); //geting correct
                //System.out.println( " size of A5 attribute possible" + attributeValues.get(attributes.get(4)).size() ); //correct

            }
            infoGain = totalEntropy - (entropyTotalAttribute);
            System.out.print(attributes.get(i) + " ");
            System.out.format("%.5f", infoGain);
            System.out.println("");
        }
    }

    public int bestAttributeList(List<Instance> instances, List<String> attributes){
        double totalEntropy = 0;
        double infoGain = 0;
        // TODO: add code here
        int value;
        double count = 0;
        double zeros = 0;
        double ones = 0;
        double entropy = 0;
        double maxinfoGain = 0;
        int sizeList;
        int bestAttribute = 0;
        double entropyTotalAttribute = 0;
         for (int i = 0; i < attributes.size(); i++) { //for the attributes
            if (attributes.get(i).equals(0)) {
            } else {
                entropyTotalAttribute = 0;
                sizeList = attributeValues.get(attributes.get(i)).size();
                for (int j = 0; j < sizeList; j++) { //for all values of the attribute
//                         if (i==4){
//                            System.out.println(entropyTotalAttribute);
//                         }
                    count = 0;
                    zeros = 0;
                    ones = 0;
                    entropy = 0;
                    for (int k = 0; k < instances.size(); k++) { //for every instance
                        // System.out.println(train.instances.get(k).attributes.get(i));
                        if (instances.get(k).attributes.get(i) == j) { //check instance if the attribute of index i is equal to j, a int value
                            count++;
                            if (instances.get(k).label == 0) {
                                zeros++;
                            }
                            if (instances.get(k).label == 1) {
                                ones++;
                            }
                        }

                    }
                    if (count == 0) {
                        entropy = 0;
                    } else {
                        entropy = -((zeros / count) * ((Math.log(zeros / count)) / Math.log(2))) - ((ones / count) * ((Math.log(ones / count)) / Math.log(2)));
                        entropy = entropy * (count / instances.size());
                    }
                    entropyTotalAttribute = entropyTotalAttribute + entropy;
                    //  System.out.println( "how many values does A5 have "  + attributeValues.get(attributes.get(4))); //geting correct
                    //System.out.println( " size of A5 attribute possible" + attributeValues.get(attributes.get(4)).size() ); //correct

                }
                infoGain = totalEntropy - (entropyTotalAttribute);
                if (infoGain > maxinfoGain) {
                    infoGain = maxinfoGain;
                    bestAttribute = i;
                }

            }
        }
        return bestAttribute;
    }
    
    
    //attribute to use next find entropy of each attribute pick highest
    public int bestAttribute(DataSet train) {


        this.labels = train.labels;
        this.attributes = train.attributes;
        this.attributeValues = train.attributeValues;
        List<Instance> inst = train.instances;
        double totalEntropy = 0;
        double infoGain = 0;
        // TODO: add code here
        int value;
        double count = 0;
        double zeros = 0;
        double ones = 0;
        double entropy = 0;
        double maxinfoGain = 0;
        int sizeList;
        int bestAttribute = 0;
        double entropyTotalAttribute = 0;


        totalEntropy = entropyNoAttribute(train);

        for (int i = 0; i < attributes.size(); i++) { //for the attributes
            if (attributes.get(i).equals(0)) {
            } else {
                entropyTotalAttribute = 0;
                sizeList = attributeValues.get(attributes.get(i)).size();
                for (int j = 0; j < sizeList; j++) { //for all values of the attribute
//                         if (i==4){
//                            System.out.println(entropyTotalAttribute);
//                         }
                    count = 0;
                    zeros = 0;
                    ones = 0;
                    entropy = 0;
                    for (int k = 0; k < train.instances.size(); k++) { //for every instance
                        // System.out.println(train.instances.get(k).attributes.get(i));
                        if (train.instances.get(k).attributes.get(i) == j) { //check instance if the attribute of index i is equal to j, a int value
                            count++;
                            if (train.instances.get(k).label == 0) {
                                zeros++;
                            }
                            if (train.instances.get(k).label == 1) {
                                ones++;
                            }
                        }

                    }
                    if (count == 0) {
                        entropy = 0;
                    } else {
                        entropy = -((zeros / count) * ((Math.log(zeros / count)) / Math.log(2))) - ((ones / count) * ((Math.log(ones / count)) / Math.log(2)));
                        entropy = entropy * (count / train.instances.size());
                    }
                    entropyTotalAttribute = entropyTotalAttribute + entropy;
                    //  System.out.println( "how many values does A5 have "  + attributeValues.get(attributes.get(4))); //geting correct
                    //System.out.println( " size of A5 attribute possible" + attributeValues.get(attributes.get(4)).size() ); //correct

                }
                infoGain = totalEntropy - (entropyTotalAttribute);
                if (infoGain > maxinfoGain) {
                    infoGain = maxinfoGain;
                    bestAttribute = i;
                }

            }
        }
        return bestAttribute;
    }

    public double entropyNoAttribute(DataSet train) {
        this.labels = train.labels;
        this.attributes = train.attributes;
        this.attributeValues = train.attributeValues;



        this.labels = train.labels;
        this.attributes = train.attributes;
        this.attributeValues = train.attributeValues;
        // TODO: add code here
        double zeros = 0;
        double ones = 0;
        double count = 0;
        double entropy;
        double value;
        for (int i = 0; i < train.instances.size(); i++) {
            value = train.instances.get(i).label;

            if (value == 0) {
                zeros++;
            } else {
                ones++;
            }
            count++;
        }
        //  System.out.println(zeros + " " + ones + " " + count);
        entropy = -((zeros / count) * ((Math.log(zeros / count)) / Math.log(2))) - ((ones / count) * ((Math.log(ones / count)) / Math.log(2)));




        return entropy;

    }
}
