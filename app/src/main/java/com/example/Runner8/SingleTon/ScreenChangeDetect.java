package com.example.Runner8.SingleTon;

import com.example.Runner8.ui.community.Adapter.Board.model.BoardModel;
import com.example.Runner8.ui.community.Adapter.Notice.model.NoticeModel;

import java.util.HashMap;
import java.util.Map;

public class ScreenChangeDetect {

    boolean removeBoard;
    boolean pickBoardToComHome;
    boolean uploadBoardToComHome;
    boolean NoticePickBoardToNotice;
    boolean SearchToPick;
    boolean HotBoardToPick;
    Map<String, Object> pickBoardMap = new HashMap<>();

    public BoardModel boardModel;
    public NoticeModel noticeModel;

    private static final ScreenChangeDetect SCREEN_CHANGE_DETECT_INSTANCE = new ScreenChangeDetect();

    public static synchronized ScreenChangeDetect getInstance() {return SCREEN_CHANGE_DETECT_INSTANCE;}

    public boolean isPickBoardToComHome() {
        return pickBoardToComHome;
    }

    public void setPickBoardToComHome(boolean pickBoardToComHome) {
        this.pickBoardToComHome = pickBoardToComHome;
    }

    public void setPickBoardMap(BoardModel boardModel) {
        this.pickBoardMap.put("board_index", boardModel.getBoard_index());
    }

    public Map<String, Object> getPickBoardMap() {
        return pickBoardMap;
    }

    public void setBoardModel(BoardModel boardModel) {
        this.boardModel = boardModel;
    }

    public BoardModel getBoardModel() {
        return boardModel;
    }

    public boolean isRemoveBoard() {
        return removeBoard;
    }

    public void setRemoveBoard(boolean removeBoard) {
        this.removeBoard = removeBoard;
    }

    public boolean isUploadBoardToComHome() {
        return uploadBoardToComHome;
    }

    public void setUploadBoardToComHome(boolean uploadBoardToComHome) {
        this.uploadBoardToComHome = uploadBoardToComHome;
    }

    public void setNoticePickBoardToNotice(boolean noticePickBoardToNotice) {
        NoticePickBoardToNotice = noticePickBoardToNotice;
    }

    public boolean isNoticePickBoardToNotice() {
        return NoticePickBoardToNotice;
    }

    public void setNoticeModel(NoticeModel noticeModel) {
        this.noticeModel = noticeModel;
    }

    public NoticeModel getNoticeModel() {
        return noticeModel;
    }

    public void setSearchToPick(boolean searchToPick) {
        SearchToPick = searchToPick;
    }

    public boolean isSearchToPick() {
        return SearchToPick;
    }

    public void setHotBoardToPick(boolean hotBoardToPick) {
        HotBoardToPick = hotBoardToPick;
    }

    public boolean isHotBoardToPick() {
        return HotBoardToPick;
    }
}
