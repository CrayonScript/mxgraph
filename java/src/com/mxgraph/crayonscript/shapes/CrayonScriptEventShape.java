package com.mxgraph.crayonscript.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.CellFrameEnum;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxCellState;

import java.awt.*;

public class CrayonScriptEventShape extends CrayonScriptBasicShape {

    public CrayonScriptEventShape(String shapeName) {
        super(ShapeStructureType.EVENT, shapeName);
    }

    @Override
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {

        initialize(state);

        Color frameColor = getParentFrameColor(state);

        Color secondColor = currentColors.get(1);

        CellFrameEnum snapToParentDropFlag = ((mxCell) state.getCell()).snapToParentDropFlag;
        if (snapToParentDropFlag != null && ((mxCell) ((mxCell) state.getCell()).getParent()).isShape())
        {
            secondColor = ((mxCell) ((mxCell) state.getCell()).getParent()).referenceShape.getFrameColor(snapToParentDropFlag);
        }

        Color paintedFirstColor = getColor(frameColor);
        Color paintedSecondColor = getColor(secondColor);

        paintRectangle(canvas, state, 0, paintedFirstColor);
        paintRectangle(canvas, state, 1, paintedSecondColor);

        drawText(canvas, ((mxCell) state.getCell()).getText(), state);
    }
}
