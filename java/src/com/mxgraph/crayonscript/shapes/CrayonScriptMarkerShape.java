package com.mxgraph.crayonscript.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxCellState;

public class CrayonScriptMarkerShape extends CrayonScriptBasicShape {

    public CrayonScriptMarkerShape(String shapeName) {
        super(ShapeStructureType.MARKER, shapeName);
    }

    @Override
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {

        initialize(state);

        paintRectangle(canvas, state, 0, ((mxCell) state.getCell()).markerColor);
    }
}
