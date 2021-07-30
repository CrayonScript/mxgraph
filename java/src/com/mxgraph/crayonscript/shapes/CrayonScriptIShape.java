package com.mxgraph.crayonscript.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.CellFrameEnum;
import com.mxgraph.model.CellGapEnum;
import com.mxgraph.shape.mxIShape;
import com.mxgraph.view.mxCellState;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public interface CrayonScriptIShape extends mxIShape {
    /**
     *
     */
    void paintShape(mxGraphics2DCanvas canvas, mxCellState state);

    ArrayList<CrayonScriptBasicShape.SvgElement> getSvgElements();

    int getSubElements();

    int getOpacity();

    Color getFrameColor();

    Color getFrameColor(CellFrameEnum frameEnum);

    RoundRectangle2D getFrame(CellFrameEnum frameEnum);

    boolean isExtender();

    double getOriginalGap(CellGapEnum gapEnum);
}
