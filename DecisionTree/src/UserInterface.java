import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;

/**
 *
 * @author Dr. Greg M. Bernstein
 */
public class UserInterface {
    static DelegateTree g;
    /** Creates a new instance of SimpleGraphView */
    public UserInterface() {
    	MyNode root = new MyNode("ROOT");
    	root.isRoot=true;
    	MyNode a = new MyNode("a");
    	MyNode b = new MyNode("b");
    	MyNode c = new MyNode("c");
        // Graph<V, E> where V is the type of the vertices and E is the type of the edges
        // Note showing the use of a SparseGraph rather than a SparseMultigraph
        g = new DelegateTree<MyNode, MyLink>();
        // Add some vertices. From above we defined these to be type Integer.
        g.addVertex(root);
        g.addChild(new MyLink(root,a),root, a);
        g.addChild(new MyLink(root,b),root, b);
        g.addChild(new MyLink(root,c),root, c);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        UserInterface sgv = new UserInterface(); // This builds the graph
        // Layout<V, E>, VisualizationComponent<V,E>        
    	Layout<MyNode, MyLink> layout ;
    	layout = new TreeLayout<MyNode, MyLink>(g,200,200);
    	
    	VisualizationViewer vv =  new VisualizationViewer<MyNode, MyLink>(layout);

        Transformer<MyNode,Paint> vertexPaint = new Transformer<MyNode,Paint>() {
            public Paint transform(MyNode node) {
            	if(node.isRoot) return Color.YELLOW;
                return Color.GREEN;
            }
        };  
        Transformer<MyNode,Shape> vertexSize = new Transformer<MyNode,Shape>(){
            public Shape transform(MyNode i){
            	if(i.isRoot) return new Ellipse2D.Double(-15,-15,80,80);
                Ellipse2D circle = new Ellipse2D.Double(-15, -15, 60, 60);
                // in this case, the vertex is twice as large
          //      if(i == 2) return AffineTransform.getScaleInstance(2, 2).createTransformedShape(circle);
                 return circle;
            }
        };
        Transformer<MyNode,Font> vertexFont = new Transformer<MyNode,Font>() {
            public Font transform(MyNode i) {
                return new Font("Comic Sans MS",Font.PLAIN,15);
            }
        };
        Transformer<MyLink,Shape> edgeShape = new Transformer<MyLink,Shape>() {
            public Shape transform(MyLink link) {
            	Line2D l = new Line2D.Double();
            	return l;          		
           }
        };
        Transformer<MyLink,Paint> edgePaint = new Transformer<MyLink,Paint>() {
            public Paint transform(MyLink link) {     	
            	return Color.DARK_GRAY;             		
           }
        }; 
        Transformer labelTransformer = new ChainedTransformer<String,String>(new Transformer[]{
                new ToStringLabeller<String>(),
                new Transformer<String,String>() {
                public String transform(String input) {
                    return "<html><font color=\"blue\">"+input.split(" ")[0];
                }}});
        
        
        vv.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(labelTransformer);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);        
        vv.getRenderContext().setVertexFontTransformer(vertexFont);
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        //vv.getRenderContext().setEdgeShapeTransformer(edgeShape);
        
        JFrame frame = new JFrame("Simple Graph View 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);     
    }
    
}
