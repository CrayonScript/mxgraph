package com.mxgraph.crayonscript.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;

public class CrayonScriptWhileShape extends CrayonScriptBasicShape {

    public CrayonScriptWhileShape(String shapeName) {
        super(ShapeStructureType.WHILE, shapeName);
    }

    @Override
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {

        initialize(state);

        paintRectangle(canvas, state, 0, getColor(currentColors.get(0)),true, true);
        paintRectangle(canvas, state, 1, getColor(currentColors.get(1)));
        paintRectangle(canvas, state, 2, getColor(currentColors.get(2)));
    }
}
