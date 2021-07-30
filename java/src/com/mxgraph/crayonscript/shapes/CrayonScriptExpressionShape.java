package com.mxgraph.crayonscript.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.CellFrameEnum;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxCellState;

import java.awt.*;

public class CrayonScriptExpressionShape extends CrayonScriptBasicShape {

    public CrayonScriptExpressionShape(String shapeName) {
        super(ShapeStructureType.EXPRESSION, shapeName);
    }

    @Override
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {

        initialize(state);

        Color frameColor = getParentFrameColor(state);

        Color secondColor = currentColors.get(1);

        CellFrameEnum snapToParentDropFlag = ((mxCell) state.getCell()).snapToParentDropFlag;
        if (snapToParentDropFlag != null && ((mxCell) state.getCell()).getParent().isShape())
        {
            secondColor = ((mxCell) ((mxCell) state.getCell()).getParent()).referenceShape.getFrameColor(snapToParentDropFlag);
        }

        paintRectangle(canvas, state, 0, getColor(frameColor));
        paintRectangle(canvas, state, 1, getColor(secondColor));

        drawText(canvas, ((mxCell) state.getCell()).getText(), state);
    }
}
