package com.example.proyectointegrador12.principal;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectointegrador12.EditNoticia;
import com.example.proyectointegrador12.R;

public class Principal extends Fragment {

    public static final String USUARIO = "com.example.proyectointegrador12.principal.Principal.USUARIO";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    public void editNoticia(View view) { }
}