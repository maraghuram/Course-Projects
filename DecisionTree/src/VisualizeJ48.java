import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;
 
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
 
 import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
 
 /**
  * Displays a trained J48 as tree.
  * Expects an ARFF filename as first argument.
  *
  * @author FracPete (fracpete at waikato dot ac dot nz)
  */
 public class VisualizeJ48 {
	 
	 public static String[] splitString(String str){
		 return str.split(",");
	 }
	 
   public static void main(String args[]) throws Exception {
     // train classifier
	 JFileChooser fc = new JFileChooser();
	 fc.setCurrentDirectory(new File("C:\\Program Files\\Weka-3-7\\data"));;
     fc.setMultiSelectionEnabled(false);
     
     FileNameExtensionFilter filter = new FileNameExtensionFilter("ARFF FILES", "arff");
     fc.setFileFilter(filter);
     fc.showOpenDialog(new JDialog());
     
	 File file = fc.getSelectedFile();
     J48 cls = new J48();
     Instances data = new Instances(new BufferedReader(new FileReader(file)));
     data.setClassIndex(data.numAttributes() - 1);
     cls.buildClassifier(data);
     
     // display classifier
     final javax.swing.JFrame jf = 
       new javax.swing.JFrame("Decision Tree Classifier Visualizer");
     
     jf.setSize(800,800);
     jf.getContentPane().setLayout(new BorderLayout());
     TreeVisualizer tv = new TreeVisualizer(null,
         cls.graph(),
         new PlaceNode2());
     jf.getContentPane().add(tv, BorderLayout.CENTER);
     jf.addWindowListener(new java.awt.event.WindowAdapter() {
       public void windowClosing(java.awt.event.WindowEvent e) {
         jf.dispose();
       }
     });
 
     jf.setVisible(true);
     tv.fitToScreen();
     
     String[][] strData = new String[data.size()][data.numAttributes()];
     for(int i=0;i<data.size();++i){
    	 strData[i] = splitString(data.instance(i).toString());
     }
     
     String[] attr = new String[data.numAttributes()];
     for(int i=0;i<data.numAttributes();++i){
    	 attr[i] = data.attribute(i).name();
     }
     
     final JFrame tableFrame = new JFrame("Input Dataset - " + file.getName());
     tableFrame.addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent e) {
             tableFrame.dispose();
           }
         });
     
     tableFrame.setSize( 400, 400 );
	 tableFrame.setBackground( Color.gray );
	 final JPanel topPanel = new JPanel();
	 topPanel.setLayout( new BorderLayout() );
	 tableFrame.getContentPane().add( topPanel );
	 JTable table = new JTable( strData, attr );
	 JScrollPane scrollPane = new JScrollPane( table );
	 topPanel.add( scrollPane, BorderLayout.CENTER );
	 
	 tableFrame.setVisible(true);
   }
 }