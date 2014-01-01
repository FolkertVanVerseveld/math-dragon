package org.teaminfty.math_dragon.view.math;

import org.teaminfty.math_dragon.view.HoverState;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class MathOperationPower extends MathBinaryOperation
{
	public static final String TYPE = "power";
	
	/** A paint that's used to draw the operator when the user is hovering over this object */
	private Paint operatorPaint = new Paint();
	
    public MathOperationPower()
    { this(null, null); }
    
	public MathOperationPower(MathObject base, MathObject power)
	{
		super(base, power);
		
		// Initialise the paint
		operatorPaint.setColor(0xcc4444ff);
	}
	
	public String toString()
	{
	    return "(" + getLeft().toString() + "^" + getRight().toString() + ")";
	}
    
    @Override
    public int getPrecedence()
    { return MathObjectPrecedence.POWER; }
    
    /**
     * Assign <tt>o</tt> to mathematical expression to base expression.
     * 
     * @param o
     *        The mathematical expression.
     */
    public void setBase(MathObject o)
    {
        setLeft(o);
    }

    /**
     * Retrieve the base mathematical expression. <b>Note:</b> <tt>null</tt>
     * may be returned.
     * 
     * @return The base mathematical expression.
     */
    public MathObject getBase()
    {
        return getLeft();
    }
    
    /**
     * Assign <tt>o</tt> to mathematical expression to exponent expression.
     * 
     * @param o
     *        The mathematical expression.
     */
    public void setExponent(MathObject o)
    {
        setRight(o);
    }

    /**
     * Retrieve the exponent mathematical expression. <b>Note:</b> <tt>null</tt>
     * may be returned.
     * 
     * @return The exponent mathematical expression.
     */
    public MathObject getExponent()
    {
        return getRight();
    }

	@Override
	public Rect[] getOperatorBoundingBoxes() 
	{
	    // Get the children sizes
	    Rect[] sizes = getChildrenSize();
	    
		// Powers don't have a visible operator
	    // However, they need an operator bounding box (for dropping other operations on them)
		return new Rect[] {
		        new Rect(0, 0, sizes[0].width(), sizes[1].height()),
                new Rect(sizes[0].width(), sizes[1].height(), sizes[0].width() + sizes[1].width(), sizes[0].height() + sizes[1].height())
	        };
	}

    /**
     * Returns the sizes of the bounding of the children.
     * 
     * @param maxWidth
     *        The maximum width the {@link MathObject} can have (can be {@link MathObject#NO_MAXIMUM})
     * @param maxHeight
     *        The maximum height the {@link MathObject} can have (can be {@link MathObject#NO_MAXIMUM})
     * @return The size of the child bounding boxes
     */
	public Rect[] getChildrenSize()
	{
		// Get the sizes both operands want to take
        Rect leftSize = getChild(0).getBoundingBox();
        Rect rightSize = getChild(1).getBoundingBox();
        
        // Return the Sizes
		return new Rect[] {leftSize, rightSize};
	}

    @Override
    public Rect getBoundingBox()
    {
        // Get the sizes
        Rect leftSize = getChild(0).getBoundingBox();
        Rect rightSize = getChild(1).getBoundingBox();
        
        // Return a bounding box, containing the bounding boxes of the children
        return new Rect(0, 0, leftSize.width() + rightSize.width(), Math.max(leftSize.height(), rightSize.height()));
    }
	
	@Override
	public Rect getChildBoundingBox(int index) throws IndexOutOfBoundsException 
	{
		// Check if the child exists
		this.checkChildIndex(index);
		
		// Get the Size of the children
		Rect[] childrenSize = getChildrenSize();
		
		// Move the bounding boxes to the correct position
		childrenSize[0].offsetTo(0, childrenSize[1].height());
		childrenSize[1].offsetTo(childrenSize[0].width(), 0);
		
		// Return the right bounding box
		return childrenSize[index];
	}
	
	@Override
	public void setLevel(int l)
	{
		level = l;
		getBase().setLevel(level);
		getExponent().setLevel(level + 1);
	}
	
	//We regard the base operand as the vertical center of the mathObject
	@Override
    public Point getCenter()
    {
    	Rect bounding_vertical = this.getChildBoundingBox(0);
    	Rect bounding_horizontal = this.getBoundingBox();
    	return new Point(bounding_horizontal.centerX(), bounding_vertical.centerY());
    }

	@Override
	public void draw(Canvas canvas) 
	{
        // Draw the bounding boxes
        drawBoundingBoxes(canvas);
        
        // Draw the operator if we're hovering
        if(state == HoverState.HOVER)
        {
            final Rect[] boxes = getOperatorBoundingBoxes();
            for(Rect box : boxes)
                canvas.drawRect(box, operatorPaint);
        }
        
        // Only draw the children
        drawChildren(canvas);
	}
	
	@Override
	protected String getType()
	{
	    return TYPE;
	}
}
