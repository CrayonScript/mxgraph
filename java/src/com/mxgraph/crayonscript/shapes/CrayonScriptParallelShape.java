package com.mxgraph.crayonscript.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;

public class CrayonScriptParallelShape extends CrayonScriptBasicShape {

    public CrayonScriptParallelShape(String shapeName) {
        super(ShapeStructureType.PARALLEL, shapeName);
    }

    @Override
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {

        initialize(state);

        paintRectangle(canvas, state, 0, getColor(currentColors.get(0)), true);
        paintRectangle(canvas, state, 1, getColor(currentColors.get(1)));
        paintRectangle(canvas, state, 2, getColor(currentColors.get(2)));
    }
}
