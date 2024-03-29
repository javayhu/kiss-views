package com.javayhu.kiss;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.javayhu.kiss.views.dialog.ChoiceDialog;
import com.javayhu.kiss.views.dialog.InfoDialog;
import com.javayhu.kiss.views.dialog.ProgressDialog;
import com.javayhu.kiss.views.dialog.DialogSaveStateHandler;
import com.javayhu.kiss.views.dialog.StandardDialog;
import com.javayhu.kiss.views.dialog.TextInputDialog;

import java.util.ArrayList;
import java.util.List;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    //This can be any numbers. R.id.* were chosen for simplicity of example
    private static final int ID_STANDARD_DIALOG = R.id.btn_standard_dialog;
    private static final int ID_SINGLE_CHOICE_DIALOG = R.id.btn_single_choice_dialog;
    private static final int ID_INFO_DIALOG = R.id.btn_info_dialog;
    private static final int ID_MULTI_CHOICE_DIALOG = R.id.btn_multi_choice_dialog;
    private static final int ID_TEXT_INPUT_DIALOG = R.id.btn_text_input_dialog;
    private static final int ID_PROGRESS_DIALOG = R.id.btn_progress_dialog;

    /*
     * This guy should be shared by multiple dialogs. You need to pass this object
     * to setInstanceStateHandler of dialog together with unique ID (it will be used to determine
     * what dialog was shown before configuration change if any.
     */
    private DialogSaveStateHandler saveStateHandler;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        saveStateHandler = new DialogSaveStateHandler();

        findViewById(R.id.btn_standard_dialog).setOnClickListener(this);
        findViewById(R.id.btn_single_choice_dialog).setOnClickListener(this);
        findViewById(R.id.btn_info_dialog).setOnClickListener(this);
        findViewById(R.id.btn_multi_choice_dialog).setOnClickListener(this);
        findViewById(R.id.btn_text_input_dialog).setOnClickListener(this);
        findViewById(R.id.btn_progress_dialog).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //View's ID correspond to our constants, so we just pass it
        showLovelyDialog(v.getId(), null);
    }

    private void showLovelyDialog(int dialogId, Bundle savedInstanceState) {
        switch (dialogId) {
            case ID_STANDARD_DIALOG:
                showStandardDialog(savedInstanceState);
                break;
            case ID_SINGLE_CHOICE_DIALOG:
                showSingleChoiceDialog(savedInstanceState);
                break;
            case ID_INFO_DIALOG:
                showInfoDialog(savedInstanceState);
                break;
            case ID_MULTI_CHOICE_DIALOG:
                showMultiChoiceDialog(savedInstanceState);
                break;
            case ID_TEXT_INPUT_DIALOG:
                showTextInputDialog(savedInstanceState);
                break;
            case ID_PROGRESS_DIALOG:
                showProgressDialog(savedInstanceState);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveStateHandler.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if (DialogSaveStateHandler.wasDialogOnScreen(savedState)) {
            //Dialog won't be restarted automatically, so we need to call this method.
            //Each dialog knows how to restore its state
            showLovelyDialog(DialogSaveStateHandler.getSavedDialogId(savedState), savedState);
        }
    }

    private void showStandardDialog(Bundle savedInstanceState) {
        new StandardDialog(this)
                .setTopColorRes(R.color.indigo)
                .setButtonsColorRes(R.color.darkDeepOrange)
                .setIcon(R.drawable.ic_star_border_white_36dp)
                .setTitle(R.string.rate_title)
                .setInstanceStateHandler(ID_STANDARD_DIALOG, saveStateHandler)
                .setSavedInstanceState(savedInstanceState)
                .setMessage(R.string.rate_message)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(DialogActivity.this,
                                R.string.repo_waiting,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .setNeutralButton(R.string.later, null)
                .setNegativeButton(android.R.string.no, null)
                .show();
    }


    private void showSingleChoiceDialog(Bundle savedInstanceState) {
        ArrayAdapter<DonationOption> adapter = new DonationAdapter(this, loadDonationOptions());
        new ChoiceDialog(this)
                .setTopColorRes(R.color.darkGreen)
                .setTitle(R.string.donate_title)
                .setInstanceStateHandler(ID_SINGLE_CHOICE_DIALOG, saveStateHandler)
                .setIcon(R.drawable.ic_local_atm_white_36dp)
                .setMessage(R.string.donate_message)
                .setItems(adapter, new ChoiceDialog.OnItemSelectedListener<DonationOption>() {
                    @Override
                    public void onItemSelected(int position, DonationOption item) {
                        Toast.makeText(DialogActivity.this,
                                getString(R.string.you_donated, item.amount),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .setSavedInstanceState(savedInstanceState)
                .show();
    }

    private void showInfoDialog(Bundle savedInstanceState) {
        new InfoDialog(this)
                .setTopColorRes(R.color.darkBlueGrey)
                .setIcon(R.drawable.ic_info_outline_white_36dp)
                .setInstanceStateHandler(ID_INFO_DIALOG, saveStateHandler)
                .setNotShowAgainOptionEnabled(0)
                .setSavedInstanceState(savedInstanceState)
                .setTitle(R.string.info_title)
                .setMessage(R.string.info_message)
                .show();
    }


    /*
     * By passing theme as a second argument to constructor - we can tint checkboxes/edittexts.
     * Don't forget to set your theme's parent to Theme.AppCompat.Light.Dialog.Alert
     */
    private void showMultiChoiceDialog(Bundle savedInstanceState) {
        String[] items = getResources().getStringArray(R.array.food);
        new ChoiceDialog(this, R.style.CheckBoxTintTheme)
                .setTopColorRes(R.color.darkRed)
                .setTitle(R.string.order_food_title)
                .setIcon(R.drawable.ic_food_white_36dp)
                .setInstanceStateHandler(ID_MULTI_CHOICE_DIALOG, saveStateHandler)
                .setItemsMultiChoice(items, new ChoiceDialog.OnItemsSelectedListener<String>() {
                    @Override
                    public void onItemsSelected(List<Integer> positions, List<String> items) {
                        Toast.makeText(DialogActivity.this,
                                getString(R.string.you_ordered, TextUtils.join("\n", items)),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                //.setConfirmButtonText(R.string.confirm)
                .setSavedInstanceState(savedInstanceState)
                .show();
    }


    private void showTextInputDialog(Bundle savedInstanceState) {
        new TextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.darkDeepOrange)
                .setTitle(R.string.text_input_title)
                .setMessage(R.string.text_input_message)
                .setIcon(R.drawable.ic_assignment_white_36dp)
                .setInstanceStateHandler(ID_TEXT_INPUT_DIALOG, saveStateHandler)
                .setInputFilter(R.string.text_input_error_message, new TextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("\\w+");
                    }
                })
                .setConfirmButton(android.R.string.ok, new TextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        Toast.makeText(DialogActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                })
                .setSavedInstanceState(savedInstanceState)
                .show();
    }


    private void showProgressDialog(Bundle savedInstanceState) {
        new ProgressDialog(this)
                .setIcon(R.drawable.ic_cast_connected_white_36dp)
                .setTitle(R.string.connecting_to_server)
                .setInstanceStateHandler(ID_PROGRESS_DIALOG, saveStateHandler)
                .setTopColorRes(R.color.teal)
                .setSavedInstanceState(savedInstanceState)
                .setCancelable(true)
                .show();
    }

    private List<DonationOption> loadDonationOptions() {
        List<DonationOption> result = new ArrayList<>();
        String[] raw = getResources().getStringArray(R.array.donations);
        for (String op : raw) {
            String[] info = op.split("%");
            result.add(new DonationOption(info[1], info[0]));
        }
        return result;
    }

}
