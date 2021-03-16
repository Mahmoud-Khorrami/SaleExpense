package com.example.boroodat.general;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ClearError implements TextWatcher
{
    private TextInputLayout textInputLayout;

    public ClearError(TextInputLayout textInputLayout)
    {
        this.textInputLayout = textInputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        textInputLayout.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable)
    {

    }
}
