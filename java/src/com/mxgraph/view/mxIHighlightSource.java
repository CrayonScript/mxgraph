package com.mxgraph.view;

import com.mxgraph.model.CellFrameEnum;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public interface mxIHighlightSource {

    RoundRectangle2D getHighlightRect();

    Rectangle getHighlightBounds();

    CellFrameEnum getHighlightDropFlag();

    RoundRectangle2D getOtherHighlightRect();

    Rectangle getOtherHighlightBounds();

    CellFrameEnum getOtherHighlightDropFlag();
}
