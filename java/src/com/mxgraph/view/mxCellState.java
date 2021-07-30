/**
 * Copyright (c) 2007, Gaudenz Alder
 */
package com.mxgraph.view;

import com.mxgraph.crayonscript.shapes.CrayonScriptBasicShape;
import com.mxgraph.model.CellFrameEnum;
import com.mxgraph.model.CellPaintMode;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents the current state of a cell in a given graph view.
 */
public class mxCellState extends mxRectangle implements mxIHighlightSource {
    /**
     *
     */
    private static final long serialVersionUID = 7588335615324083354L;

    /**
     * Reference to the enclosing graph view.
     */
    protected mxGraphView view;

    /**
     * Reference to the cell that is represented by this state.
     */
    protected Object cell;

    /**
     * Holds the current label value, including newlines which result from
     * word wrapping.
     */
    protected String label;

    /**
     * Contains an array of key, value pairs that represent the style of the
     * cell.
     */
    protected Map<String, Object> style;

    /**
     * Holds the origin for all child cells.
     */
    protected mxPoint origin = new mxPoint();

    /**
     * List of mxPoints that represent the absolute points of an edge.
     */
    protected List<mxPoint> absolutePoints;

    /**
     * Holds the absolute offset. For edges, this is the absolute coordinates
     * of the label position. For vertices, this is the offset of the label
     * relative to the top, left corner of the vertex.
     */
    protected mxPoint absoluteOffset = new mxPoint();

    /**
     * Caches the distance between the end points and the length of an edge.
     */
    protected double terminalDistance, length;

    /**
     * Array of numbers that represent the cached length of each segment of the
     * edge.
     */
    protected double[] segments;

    /**
     * Holds the rectangle which contains the label.
     */
    protected mxRectangle labelBounds;

    /**
     * Holds the largest rectangle which contains all rendering for this cell.
     */
    protected mxRectangle boundingBox;

    /**
     * Specifies if the state is invalid. Default is true.
     */
    protected boolean invalid = true;

    /**
     * Caches the visible source and target terminal states.
     */
    protected mxCellState visibleSourceState, visibleTargetState;

    /**
     * Constructs an empty cell state.
     */
    public mxCellState() {
        this(null, null, null);
    }

    /**
     * Constructs a new object that represents the current state of the given
     * cell in the specified view.
     *
     * @param view  Graph view that contains the state.
     * @param cell  Cell that this state represents.
     * @param style Array of key, value pairs that constitute the style.
     */
    public mxCellState(mxGraphView view, Object cell, Map<String, Object> style) {
        setView(view);
        setCell(cell);
        setStyle(style);
    }

    /**
     * Returns true if the state is invalid.
     */
    public boolean isInvalid() {
        return invalid;
    }

    /**
     * Sets the invalid state.
     */
    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    /**
     * Returns the enclosing graph view.
     *
     * @return the view
     */
    public mxGraphView getView() {
        return view;
    }

    /**
     * Sets the enclosing graph view.
     *
     * @param view the view to set
     */
    public void setView(mxGraphView view) {
        this.view = view;
    }

    /**
     * Returns the current label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the current label.
     */
    public void setLabel(String value) {
        label = value;
    }

    /**
     * Returns the cell that is represented by this state.
     *
     * @return the cell
     */
    public Object getCell() {
        return cell;
    }

    /**
     * Sets the cell that this state represents.
     *
     * @param cell the cell to set
     */
    public void setCell(Object cell) {
        this.cell = cell;
    }

    /**
     * Returns the cell style as a map of key, value pairs.
     *
     * @return the style
     */
    public Map<String, Object> getStyle() {
        return style;
    }

    /**
     * Sets the cell style as a map of key, value pairs.
     *
     * @param style the style to set
     */
    public void setStyle(Map<String, Object> style) {
        this.style = style;
    }

    /**
     * Returns the origin for the children.
     *
     * @return the origin
     */
    public mxPoint getOrigin() {
        return origin;
    }

    /**
     * Sets the origin for the children.
     *
     * @param origin the origin to set
     */
    public void setOrigin(mxPoint origin) {
        this.origin = origin;
    }

    /**
     * Returns the absolute point at the given index.
     *
     * @return the mxPoint at the given index
     */
    public mxPoint getAbsolutePoint(int index) {
        return absolutePoints.get(index);
    }

    /**
     * Returns the absolute point at the given index.
     *
     * @return the mxPoint at the given index
     */
    public mxPoint setAbsolutePoint(int index, mxPoint point) {
        return absolutePoints.set(index, point);
    }

    /**
     * Returns the number of absolute points.
     *
     * @return the absolutePoints
     */
    public int getAbsolutePointCount() {
        return (absolutePoints != null) ? absolutePoints.size() : 0;
    }

    /**
     * Returns the absolute points.
     *
     * @return the absolutePoints
     */
    public List<mxPoint> getAbsolutePoints() {
        return absolutePoints;
    }

    /**
     * Returns the absolute points.
     *
     * @param absolutePoints the absolutePoints to set
     */
    public void setAbsolutePoints(List<mxPoint> absolutePoints) {
        this.absolutePoints = absolutePoints;
    }

    /**
     * Returns the absolute offset.
     *
     * @return the absoluteOffset
     */
    public mxPoint getAbsoluteOffset() {
        return absoluteOffset;
    }

    /**
     * Returns the absolute offset.
     *
     * @param absoluteOffset the absoluteOffset to set
     */
    public void setAbsoluteOffset(mxPoint absoluteOffset) {
        this.absoluteOffset = absoluteOffset;
    }

    /**
     * Returns the terminal distance.
     *
     * @return the terminalDistance
     */
    public double getTerminalDistance() {
        return terminalDistance;
    }

    /**
     * Sets the terminal distance.
     *
     * @param terminalDistance the terminalDistance to set
     */
    public void setTerminalDistance(double terminalDistance) {
        this.terminalDistance = terminalDistance;
    }

    /**
     * Returns the length.
     *
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * Sets the length.
     *
     * @param length the length to set
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Returns the length of the segments.
     *
     * @return the segments
     */
    public double[] getSegments() {
        return segments;
    }

    /**
     * Sets the length of the segments.
     *
     * @param segments the segments to set
     */
    public void setSegments(double[] segments) {
        this.segments = segments;
    }

    /**
     * Returns the label bounds.
     *
     * @return Returns the label bounds for this state.
     */
    public mxRectangle getLabelBounds() {
        return labelBounds;
    }

    /**
     * Sets the label bounds.
     *
     * @param labelBounds
     */
    public void setLabelBounds(mxRectangle labelBounds) {
        this.labelBounds = labelBounds;
    }

    /**
     * Returns the bounding box.
     *
     * @return Returns the bounding box for this state.
     */
    public mxRectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * Sets the bounding box.
     *
     * @param boundingBox
     */
    public void setBoundingBox(mxRectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * Returns the rectangle that should be used as the perimeter of the cell.
     * This implementation adds the perimeter spacing to the rectangle
     * defined by this cell state.
     *
     * @return Returns the rectangle that defines the perimeter.
     */
    public mxRectangle getPerimeterBounds() {
        return getPerimeterBounds(0);
    }

    /**
     * Returns the rectangle that should be used as the perimeter of the cell.
     *
     * @return Returns the rectangle that defines the perimeter.
     */
    public mxRectangle getPerimeterBounds(double border) {
        mxRectangle bounds = new mxRectangle(getRectangle());

        if (border != 0) {
            bounds.grow(border);
        }

        return bounds;
    }

    /**
     * Sets the first or last point in the list of points depending on isSource.
     *
     * @param point    Point that represents the terminal point.
     * @param isSource Boolean that specifies if the first or last point should
     *                 be assigned.
     */
    public void setAbsoluteTerminalPoint(mxPoint point, boolean isSource) {
        if (isSource) {
            if (absolutePoints == null) {
                absolutePoints = new ArrayList<mxPoint>();
            }

            if (absolutePoints.size() == 0) {
                absolutePoints.add(point);
            } else {
                absolutePoints.set(0, point);
            }
        } else {
            if (absolutePoints == null) {
                absolutePoints = new ArrayList<mxPoint>();
                absolutePoints.add(null);
                absolutePoints.add(point);
            } else if (absolutePoints.size() == 1) {
                absolutePoints.add(point);
            } else {
                absolutePoints.set(absolutePoints.size() - 1, point);
            }
        }
    }

    /**
     * Returns the visible source or target terminal cell.
     *
     * @param source Boolean that specifies if the source or target cell should be
     *               returned.
     */
    public Object getVisibleTerminal(boolean source) {
        mxCellState tmp = getVisibleTerminalState(source);

        return (tmp != null) ? tmp.getCell() : null;
    }

    /**
     * Returns the visible source or target terminal state.
     *
     * @param source that specifies if the source or target state should be
     *               returned.
     */
    public mxCellState getVisibleTerminalState(boolean source) {
        return (source) ? visibleSourceState : visibleTargetState;
    }

    /**
     * Sets the visible source or target terminal state.
     *
     * @param terminalState Cell state that represents the terminal.
     * @param source        Boolean that specifies if the source or target state should be set.
     */
    public void setVisibleTerminalState(mxCellState terminalState,
                                        boolean source) {
        if (source) {
            visibleSourceState = terminalState;
        } else {
            visibleTargetState = terminalState;
        }
    }

    public mxIHighlightSource getHighlightSource() {
        return this;
    }

    public RoundRectangle2D getHighlightRect() {
        RoundRectangle2D roundedRect = ((mxCell) cell).hotspotRect;
        roundedRect = (RoundRectangle2D) roundedRect.clone();
        roundedRect.setFrame(0, 0, roundedRect.getWidth(), roundedRect.getHeight());
        return roundedRect;
    }

    public Rectangle getHighlightBounds() {
        RoundRectangle2D roundedRect = ((mxCell) cell).hotspotRect;
        return roundedRect.getBounds();
    }

    public CellFrameEnum getHighlightDropFlag() {
        return ((mxCell) this.cell).hotSpotDropFlag;
    }

    @Override
    public RoundRectangle2D getOtherHighlightRect() {
        mxCell otherCell = (mxCell) ((mxCell) cell).otherCell;
        if (((mxCell) cell).otherCell != null) {
            RoundRectangle2D roundedRect = otherCell.hotspotRect;
            if (roundedRect != null) {
                roundedRect = (RoundRectangle2D) roundedRect.clone();
                roundedRect.setFrame(0, 0, roundedRect.getWidth(), roundedRect.getHeight());
                return roundedRect;
            }
        }
        return null;
    }

    @Override
    public Rectangle getOtherHighlightBounds() {
        RoundRectangle2D roundedRect = ((mxCell) ((mxCell) cell).otherCell).hotspotRect;
        return roundedRect.getBounds();
    }

    @Override
    public CellFrameEnum getOtherHighlightDropFlag() {
        mxCell otherCell = (mxCell) ((mxCell) cell).otherCell;
        return otherCell.hotSpotDropFlag;
    }

    public String getCellText()
    {
        return ((mxCell) cell).getText();
    }

    public void setCellText(String text)
    {
        ((mxCell) cell).setText(text);
        refreshGraph();
    }

    public void refreshGraph()
    {
        view.invalidate(cell);
        view.updateCellState(this);
        view.getGraph().refresh();
    }

    public ArrayList<RoundRectangle2D> getCurrentRoundRectangles()
    {
        ArrayList<RoundRectangle2D> scaledRoundRectangles = new ArrayList<>();
        mxCell thisCell = (mxCell) cell;
        Rectangle stateRect = getRectangle();
        ArrayList<RoundRectangle2D> roundedRectangles = thisCell.getUnscaledRoundRectangles();
        RoundRectangle2D first = roundedRectangles.get(0);
        scaledRoundRectangles.add(thisCell.scaleRectangle(stateRect, first, first));
        for (int i = 1; i < roundedRectangles.size(); i++) {
            RoundRectangle2D rest = roundedRectangles.get(i);
            scaledRoundRectangles.add(thisCell.scaleRectangle(stateRect, first, rest));
        }
        return scaledRoundRectangles;
    }

    public ArrayList<RoundRectangle2D> getAllOuterRectangles()
    {
        ArrayList<RoundRectangle2D> allRoundRectangles = new ArrayList<>();
        mxCell thisCell = (mxCell) getCell();
        ArrayList<RoundRectangle2D> currentRoundRectangles = getCurrentRoundRectangles();
        allRoundRectangles.add(currentRoundRectangles.get(0));
        for (int childIndex = 0; childIndex < thisCell.getChildCount(); childIndex++)
        {
            mxCell childCell = (mxCell) thisCell.getChildAt(childIndex);
            mxCellState childCellState = getView().getState(childCell, true);
            allRoundRectangles.addAll(childCellState.getAllInnerRectangles());
        }
        return allRoundRectangles;
    }

    public ArrayList<RoundRectangle2D> getAllInnerRectangles()
    {
        ArrayList<RoundRectangle2D> allRoundRectangles = new ArrayList<>();
        mxCell thisCell = (mxCell) getCell();
        ArrayList<RoundRectangle2D> currentRoundRectangles = getCurrentRoundRectangles();
        allRoundRectangles.addAll(currentRoundRectangles.subList(1, currentRoundRectangles.size()));
        for (int childIndex = 0; childIndex < thisCell.getChildCount(); childIndex++)
        {
            mxCell childCell = (mxCell) thisCell.getChildAt(childIndex);
            mxCellState childCellState = getView().getState(childCell, true);
            allRoundRectangles.addAll(childCellState.getAllInnerRectangles());
        }
        return allRoundRectangles;
    }

    public Rectangle2D getExtendedPaintedRect()
    {
        Rectangle2D paintedRect = null;
        ArrayList<RoundRectangle2D> thisPaintedRectangles = getCurrentRoundRectangles();
        mxCell thisCell = (mxCell) getCell();
        CellPaintMode cellPaintMode = thisCell.calcPaintMode();
        if (cellPaintMode == CellPaintMode.FRAME_IN_FRAME)
        {
            paintedRect = thisPaintedRectangles.get(1).getFrame();
            Rectangle2D.union(paintedRect, thisPaintedRectangles.get(thisPaintedRectangles.size()-1).getFrame(), paintedRect);
        }
        else
        {
            paintedRect = thisPaintedRectangles.get(0).getFrame();
        }
        for (int childIndex = 0; childIndex < thisCell.getChildCount(); childIndex++)
        {
            mxCell childCell = (mxCell) thisCell.getChildAt(childIndex);
            mxCellState childCellState = getView().getState(childCell, true);
            Rectangle2D childPaintedRect = childCellState.getExtendedPaintedRect();
            Rectangle2D.union(paintedRect, childPaintedRect, paintedRect);
        }
        return paintedRect;
    }

    public Rectangle2D getPaintedRect()
    {
        Rectangle2D paintedRect = null;
        ArrayList<RoundRectangle2D> thisPaintedRectangles = getCurrentRoundRectangles();
        mxCell thisCell = (mxCell) getCell();
        CellPaintMode cellPaintMode = thisCell.calcPaintMode();
        if (cellPaintMode == CellPaintMode.FRAME_IN_FRAME)
        {
            paintedRect = thisPaintedRectangles.get(1).getFrame();
            Rectangle2D.union(paintedRect, thisPaintedRectangles.get(thisPaintedRectangles.size()-1).getFrame(), paintedRect);
        }
        else
        {
            paintedRect = thisPaintedRectangles.get(0).getFrame();
        }
        return paintedRect;
    }

    public ArrayList<RoundRectangle2D> getPaintedRectangles()
    {
        ArrayList<RoundRectangle2D> paintedRoundRectangles = new ArrayList<>();
        ArrayList<RoundRectangle2D> roundRectangles = getCurrentRoundRectangles();
        mxCell thisCell = (mxCell) getCell();
        CellPaintMode cellPaintMode = thisCell.calcPaintMode();
        RoundRectangle2D first = roundRectangles.get(0);
        if (cellPaintMode != CellPaintMode.FRAME_IN_FRAME)
        {
            paintedRoundRectangles.add(first);
        }
        for (int i = 1; i < roundRectangles.size(); i++) {
            RoundRectangle2D rest = roundRectangles.get(i);
            paintedRoundRectangles.add(rest);
        }
        return paintedRoundRectangles;
    }

    public Rectangle getEditorBounds()
    {
        Rectangle rect = getRectangle();
        // get the editor offset
        Rectangle cellRect = ((mxCell) cell).getCellEditorBounds();
        double scale = getView().scale;
        rect.x += (int) (cellRect.x*scale);
        rect.y += (int) (cellRect.y*scale);
        rect.width = (int) (cellRect.width*scale);
        rect.height = (int) (cellRect.height*scale);
        return rect;
    }

    public void updateHotspots(Object[] dragCells, Rectangle previewBounds, int x, int y,
                               double hotspot, int min, int max) {
        ((mxCell) cell).otherCell = null;

        if (dragCells != null && dragCells.length > 0) {
            ((mxCell) cell).otherCell = dragCells[0];
            ((mxCell) ((mxCell) cell).otherCell).hotspotRect = null;
        }
        ((mxCell) this.cell).hotspotRect = null;
        ((mxCell) this.cell).isHotspot = intersects(previewBounds, x, y, hotspot, min, max);
    }

    /**
     * Returns true if the given coordinate pair intersects the hotspot of the
     * given state.
     *
     * @param previewBounds
     * @param x
     * @param y
     * @param hotspot
     * @param min
     * @param max
     * @return
     */
    protected boolean intersects(Rectangle previewBounds, int x, int y,
                                 double hotspot, int min, int max) {
        if (hotspot <= 0) {
            return false;
        }

        mxCell otherCell = (mxCell) ((mxCell) cell).otherCell;

        if (otherCell == null) {
            return false;
        }
        if (!(otherCell instanceof mxICell)
                || !((mxICell) otherCell).isShape() || !((mxICell) otherCell).isDropSource()) {
            return false;
        }
        if (cell == null || !(cell instanceof mxICell)
                || !((mxICell) cell).isShape() || !((mxICell) cell).isDropTarget()) {
            return false;
        }

        CellFrameEnum[] dropSourceFlags = ((mxICell) otherCell).getDropSourceFlags();
        CellFrameEnum[] dropTargetFlags = ((mxICell) cell).getDropTargetFlags();

        if ((dropTargetFlags == null || dropSourceFlags.length == 0) ||
                (dropTargetFlags == null || dropTargetFlags.length == 0)) return false;


        mxCellState sourceState = new mxCellState(getView(), otherCell, getView().getGraph().getCellStyle(cell));
        getView().updateCellState(sourceState);

        sourceState.setX(previewBounds.getX());
        sourceState.setY(previewBounds.getY());

        Rectangle sourceStateRect = sourceState.getRectangle();

        mxCell sourceCell = (mxCell) otherCell;
        mxCell targetCell = (mxCell) cell;

        List<RoundRectangle2D> sourceRectangles = sourceCell.getUnscaledRoundRectangles();
        List<RoundRectangle2D> targetRectangles = targetCell.getUnscaledRoundRectangles();

        Rectangle stateRect = getRectangle();

        for (int sourceFlagIndex = 0; sourceFlagIndex < dropSourceFlags.length; sourceFlagIndex++)
        {
            for (int targetFlagIndex = 0; targetFlagIndex < dropTargetFlags.length; targetFlagIndex++)
            {
                CellFrameEnum dropSourceFlag = dropSourceFlags[sourceFlagIndex];
                CellFrameEnum dropTargetFlag = dropTargetFlags[targetFlagIndex];

                RoundRectangle2D sourceRect = CrayonScriptBasicShape.scaleRectangle(
                        sourceStateRect,
                        sourceRectangles.get(0),
                        sourceRectangles.get(dropSourceFlag.bitIndex));
                RoundRectangle2D targetRect = CrayonScriptBasicShape.scaleRectangle(
                        stateRect,
                        targetRectangles.get(0),
                        targetRectangles.get(dropTargetFlag.bitIndex));

                if (intersects(sourceRect, targetRect, hotspot, min, max))
                {
                    // if a block shape is being dropped onto the native target then allow OUTER as the source flag
                    // else if the block shape is being dropped onto a template target then allow OUTER as the source flag
                    // else disallow OUTER as the source flag
                    if (((mxCell) otherCell).isInline() || dropSourceFlag != CellFrameEnum.OUTER || (((mxCell) cell).isTemplate() || ((mxCell) cell).isNative()))
                    {
                        ((mxCell) cell).hotspotRect = targetRect;
                        ((mxCell) cell).hotSpotDropFlag = dropTargetFlag;

                        ((mxCell) otherCell).hotspotRect = sourceRect;
                        ((mxCell) otherCell).hotSpotDropFlag = dropSourceFlag;

                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected boolean intersects(RoundRectangle2D sourceRect, RoundRectangle2D targetRect, double hotspot, int min, int max)
    {
        double x = sourceRect.getCenterX();
        double y = sourceRect.getCenterY();

        RoundRectangle2D rectangle2D = targetRect;

        if (hotspot > 0) {
            int cx = (int) Math.round(rectangle2D.getCenterX());
            int cy = (int) Math.round(rectangle2D.getCenterY());
            int width = (int) Math.round(rectangle2D.getWidth());
            int height = (int) Math.round(rectangle2D.getHeight());

            int w = (int) Math.max(min, width * hotspot);
            int h = (int) Math.max(min, height * hotspot);

            if (max > 0) {
                w = Math.min(w, max);
                h = Math.min(h, max);
            }

            Rectangle rect = new Rectangle(Math.round(cx - w / 2),
                    Math.round(cy - h / 2), w, h);

            return rect.contains(x, y);
        }

        return false;
    }

    /**
     * Returns a clone of this state where all members are deeply cloned
     * except the view and cell references, which are copied with no
     * cloning to the new instance.
     */
    public Object clone() {
        mxCellState clone = new mxCellState(view, cell, style);

        if (label != null) {
            clone.label = label;
        }

        if (absolutePoints != null) {
            clone.absolutePoints = new ArrayList<mxPoint>();

            for (int i = 0; i < absolutePoints.size(); i++) {
                clone.absolutePoints.add((mxPoint) absolutePoints.get(i)
                        .clone());
            }
        }

        if (origin != null) {
            clone.origin = (mxPoint) origin.clone();
        }

        if (absoluteOffset != null) {
            clone.absoluteOffset = (mxPoint) absoluteOffset.clone();
        }

        if (labelBounds != null) {
            clone.labelBounds = (mxRectangle) labelBounds.clone();
        }

        if (boundingBox != null) {
            clone.boundingBox = (mxRectangle) boundingBox.clone();
        }

        clone.terminalDistance = terminalDistance;
        clone.segments = segments;
        clone.length = length;
        clone.x = x;
        clone.y = y;
        clone.width = width;
        clone.height = height;

        return clone;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append(getClass().getSimpleName());
        builder.append(" [");
        builder.append("cell=");
        builder.append(cell);
        builder.append(", label=");
        builder.append(label);
        builder.append(", x=");
        builder.append(x);
        builder.append(", y=");
        builder.append(y);
        builder.append(", width=");
        builder.append(width);
        builder.append(", height=");
        builder.append(height);
        builder.append("]");

        return builder.toString();
    }

}
