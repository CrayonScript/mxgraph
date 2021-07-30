package com.mxgraph.crayonscript.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;

public class CrayonScriptWaitForShape extends CrayonScriptBasicShape {

    public CrayonScriptWaitForShape(String shapeName) {
        super(ShapeStructureType.WAIT_FOR, shapeName);
    }

    @Override
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {

        initialize(state);

        paintRectangle(canvas, state, 0, getColor(currentColors.get(0)), true);
        paintRectangle(canvas, state, 1, getColor(currentColors.get(1)));
    }
}
