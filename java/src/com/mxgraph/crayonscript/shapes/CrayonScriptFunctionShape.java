package com.mxgraph.crayonscript.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.CellFrameEnum;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxCellState;

import java.awt.*;
import java.util.ArrayList;

public class CrayonScriptFunctionShape extends CrayonScriptBasicShape {

    public CrayonScriptFunctionShape(String shapeName) {
        super(ShapeStructureType.FUNCTION, shapeName);
    }

    @Override
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {

        initialize(state);

        ArrayList<SvgElement> svgElements = getSvgElements();

        SvgElement first = svgElements.get(0);
        SvgElement second = svgElements.get(1);

        Color frameColor = getParentFrameColor(state);

        Color secondColor = second.fillColor;

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
