package tries;

import java.awt.Color;
import java.awt.Paint;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.VisualizationViewer;

public class SearchAnimation {
		
	Timer T;
	int counter,length;
	MyNode ptr,root;
	String search;
	@SuppressWarnings("rawtypes")
	VisualizationViewer vv;	
	JDialog jd;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SearchAnimation(MyNode root,VisualizationViewer vv,String input,JDialog jd)
	{		
		this.root=ptr=root;
		T=new Timer();
		counter=0;		
		this.jd=jd;
		
		length=input.length();	
		search=input;
		this.vv=vv;
		ptr.flag=true;
        vv.getRenderContext().setVertexFillPaintTransformer(
        		new Transformer<MyNode, Paint>() {
                    
                    public Paint transform(MyNode node) {                    	
                    	if(node.flag)return Color.WHITE;
                        return Color.GREEN;
                    }
        		});
                    
        vv.repaint();
	
		T.scheduleAtFixedRate(new Task(), 1000, 1000);
	}
	
	@SuppressWarnings("unchecked")
	public void CancelTimer()
	{
		T.cancel();		

		
        vv.getRenderContext().setVertexFillPaintTransformer(
        		new Transformer<MyNode, Paint>() {                            
                    public Paint transform(MyNode node) {
                    	if(node.isRoot) return Color.YELLOW;
                        return Color.GREEN;
                    }
        		});
        
        ptr=root;
        
        for(int i=0;i<search.length();++i)
        {        	
        	ptr=ptr.keys[search.charAt(i)];
        	if(ptr==null) break;
        	else ptr.flag=false;
        }
                    
        vv.repaint();       
		
	}
	
	class Task extends TimerTask
	{

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(counter<length)
			{					
				
				if(ptr.keys[(int)search.charAt(counter)]==null)
				{
					System.out.println("Failed");
					
					CancelTimer();
				}
				else
				{
					
					ptr=ptr.keys[(int)search.charAt(counter)];
					ptr.flag=true;
	                vv.getRenderContext().setVertexFillPaintTransformer(
	                		new Transformer<MyNode, Paint>() {                            
	                            public Paint transform(MyNode node) {
	                            	if(node.flag)return Color.WHITE;
	                                return Color.GREEN;
	                            }
	                		});
	                            
	                vv.repaint();                
	                
					++counter;
					
				}

				
			}
			else
			{
				CancelTimer();
			}
			
		}
		
	}

}
