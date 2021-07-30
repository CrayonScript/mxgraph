package com.mxgraph.crayonscript.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;

public class CrayonScriptGraphTemplateShape extends CrayonScriptBasicShape {

    public CrayonScriptGraphTemplateShape(String shapeName) {
        super(ShapeStructureType.TEMPLATE, shapeName);
    }

    @Override
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {

        initialize(state);

        paintRectangle(canvas, state, 0, currentColors.get(0) /* always opaque */,true);
    }
}
