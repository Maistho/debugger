package com.debugger;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class UndoRedoEnabler {
    private final List<Edit> history = new LinkedList<Edit>();
    private ListIterator<Edit> historyIterator = history.listIterator();
    private int maxHistorySize;
    private boolean locked = false;

    TextView textView;
    TextChangeListener textChangeListener;

    public UndoRedoEnabler(TextView textView) {
        this.textView = textView;
        maxHistorySize = -1;
        textChangeListener = new TextChangeListener();
        textView.addTextChangedListener(textChangeListener);
    }

    public UndoRedoEnabler(TextView textView, int maxHistorySize) {
        this.textView = textView;
        this.maxHistorySize = maxHistorySize;
        textChangeListener = new TextChangeListener();
        textView.addTextChangedListener(textChangeListener);
    }

    public void removeListener() {
        textView.removeTextChangedListener(textChangeListener);
    }

    public void clearHistory() {
        history.clear();
        historyIterator = history.listIterator();
    }

    public void undo() {
        if (canUndo()) {
            Edit edit = historyIterator.previous();
            Editable text = textView.getEditableText();

            if (text == null) return;

            locked = true;
            text.replace(edit.start, edit.start + edit.newText.length(), edit.oldText);
            locked = false;

            Selection.setSelection(text, edit.start + edit.oldText.length());
        }
    }

    public void redo() {
        System.out.println("here");
        if (canRedo()) {
            System.out.println("inside");
            for (Edit e : history)
                System.out.println(e);
            Edit edit = historyIterator.next();
            Editable text = textView.getEditableText();

            if (text == null) return;

            locked = true;
            text.replace(edit.start, edit.start + edit.oldText.length(), edit.newText);
            locked = false;

            textView.clearComposingText();
            //BaseImputConnection.removeComposingSpans();

            Selection.setSelection(text, edit.start + edit.newText.length());
        }
    }

    public boolean canUndo() {
        return historyIterator.hasPrevious();
    }

    public boolean canRedo() {
        return historyIterator.hasNext();
    }


    private Edit getLastEdit() {
        if (canUndo()) {
            historyIterator.previous();
            return historyIterator.next();
        }
        return null;
    }

    /*
        public void setMaxHistorySize(int maxHistorySize) {
            //NYI - requires trimming of history if this.maxHistorySize > maxHistorySize
        }
        */

    private void addItem(Edit item) {
        while (canRedo()) {
            historyIterator.next();
            historyIterator.remove();
        }

        historyIterator.add(item);

        if (maxHistorySize >= 0 && history.size() > maxHistorySize) {
            historyIterator = history.listIterator();
            historyIterator.remove();
            //do while (history.size() > maxHistorySize);
            historyIterator = history.listIterator(history.size());
        }
    }

    private final class TextChangeListener implements TextWatcher {
        private CharSequence oldText;

        private long lastEditTime = 0;
        private final long DELTA_TIME = 1000;

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (locked) return;
            oldText = s.subSequence(start, start + count);

            System.out.println("beforeText: " + oldText.toString());
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (locked) return;

            CharSequence newText = s.subSequence(start, start + count);

            System.out.println("afterText: " + newText.toString());
            EditType t;

            if (oldText.length() == 0 && newText.length() > 0) {
                t = EditType.INSERT;
            } else if (oldText.length() > 0 && newText.length() == 0) {
                t = EditType.REMOVE;
            } else {
                t = EditType.REPLACE;
            }

            Edit lastEdit = getLastEdit();

            if (System.currentTimeMillis() - lastEditTime > DELTA_TIME || lastEdit == null
                    || t != lastEdit.editType || t == EditType.REPLACE) {
                addItem(new Edit(start, oldText, newText, t));

            } else if (t == EditType.REMOVE) {
                lastEdit.start = start;
                lastEdit.oldText = TextUtils.concat(oldText, lastEdit.oldText);
            } else {
                lastEdit.newText = TextUtils.concat(lastEdit.newText, newText);
            }
            lastEditTime = System.currentTimeMillis();
        }

        public void afterTextChanged(Editable s) {}
    }


    private enum EditType {
        INSERT, REMOVE, REPLACE
    }

    private final class Edit {
        private int start;
        private CharSequence oldText;
        private CharSequence newText;
        private EditType editType;

        public Edit(int start, CharSequence oldText, CharSequence newText,
                    EditType editType) {
            this.start = start;
            this.oldText = oldText; // != null ? newText : "";
            this.newText = newText; // != null ? newText : "";
            this.editType = editType;
        }

        @Override
        public String toString() {
            return "Edit{"
                    + "EditType: " + editType
                    + ", Start: " + start
                    + ", Old text: \"" + oldText
                    + "\", New text: \"" + newText
                    + "\"}";
        }
    }

}
