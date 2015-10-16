// Generated code from Butter Knife. Do not modify!
package com.liaobb.evernote.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ReadNoteActivity$$ViewInjector<T extends com.liaobb.evernote.ui.ReadNoteActivity> extends com.liaobb.evernote.ui.ToolbarActivity$$ViewInjector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    super.inject(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131624053, "field 'noteRead'");
    target.noteRead = finder.castView(view, 2131624053, "field 'noteRead'");
  }

  @Override public void reset(T target) {
    super.reset(target);

    target.noteRead = null;
  }
}
