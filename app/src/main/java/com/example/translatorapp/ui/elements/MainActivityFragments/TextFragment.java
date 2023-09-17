package com.example.translatorapp.ui.elements.MainActivityFragments;

import android.Manifest;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.translatorapp.R;
import com.example.translatorapp.databinding.FragmentTextBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.Locale;


public class TextFragment extends Fragment {

    FragmentTextBinding binding;
    private TextRecognizer textRecognizer;
    private Spinner fromLang,toLang;
    private TextToSpeech textToSpeech;
    private TranslatorOptions translatorOptions;
    private DownloadConditions conditions ;
    private Translator translator;
    private static final String URI_KEY = "KEY_009";

/*
"Spanish",
            "French",
            "German",
            "Italian",
            "Portuguese",
            "Dutch",
            "Russian",
            "Chinese",
            "Japanese",
            "Korean",
            "Arabic",
            "Turkish",
             "Bengali",
            "Urdu",
            "Malay",
            "Thai",
            "Marathi",
            "Telugu",
            "Tamil",
            "Kannada"
 */

    private static String[] LangList = {
            "English",
            "Hindi",
            "Spanish",
            "French",
            "German",
            "Italian",
            "Portuguese",
            "Dutch",
            "Russian",
            "Chinese",
            "Japanese",
            "Korean",
            "Arabic",
            "Turkish",
            "Bengali",
            "Urdu",
            "Malay",
            "Thai",
            "Marathi",
            "Telugu",
            "Tamil",
            "Kannada"

    };
    private String fromLangCode,toLangCode;
    private Uri GalData;

    public TextFragment() {
        // Required empty public constructor
    }


    public static TextFragment getInstance(Uri galData)
    {
        TextFragment textFragment = new TextFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(URI_KEY, galData);
        textFragment.setArguments(bundle);
        return textFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
        {
            this.GalData = getArguments().getParcelable(URI_KEY);
            try {
                ImageToText(GalData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTextBinding.inflate(getLayoutInflater());
        fromLang = binding.fromLang;
        toLang = binding.toLang;




        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.spinner_list_view,LangList);
        adapter.setDropDownViewResource(R.layout.spinner_list_view);
        fromLang.setAdapter(adapter);
        toLang.setAdapter(adapter);
        binding.toTextbox.setMovementMethod(new ScrollingMovementMethod());

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });

        binding.swapLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fPos =  fromLang.getSelectedItemPosition();
                int tPos = toLang.getSelectedItemPosition();
                toLang.setSelection(fPos,true);
                fromLang.setSelection(tPos,true);
                if(textToSpeech.isSpeaking())
                {
                    textToSpeech.stop();
                }
                if(!binding.toTextbox.getText().toString().trim().isEmpty()) {
                    binding.fromTextbox.setText(binding.toTextbox.getText().toString().trim());

                }

            }
        });
        fromLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLangCode = GetLangCode(LangList[i]);
                String text = binding.fromTextbox.getText().toString().trim();
                if(textToSpeech.isSpeaking())
                {
                    textToSpeech.stop();
                }
                if(!text.isEmpty())
                {
                    TranslateNow(text,fromLangCode,toLangCode);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.toLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLangCode = GetLangCode(LangList[i]);
                String text = binding.fromTextbox.getText().toString().trim();
                if(textToSpeech.isSpeaking())
                {
                    textToSpeech.stop();
                }
                if(!text.isEmpty())
                {
                    TranslateNow(text,fromLangCode,toLangCode);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.fromTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(fromLangCode!=null && toLangCode!=null && !charSequence.toString().trim().isEmpty() )
                {
                    TranslateNow(charSequence.toString().trim(),fromLangCode,toLangCode);

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(fromLangCode!=null && toLangCode!=null && !charSequence.toString().trim().isEmpty() )
                {
                    TranslateNow(charSequence.toString().trim(),fromLangCode,toLangCode);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


                if(fromLangCode!=null && toLangCode!=null )
                {
                    TranslateNow(editable.toString().trim(),fromLangCode,toLangCode);

                }

            }
        });

        binding.fromAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.fromTextbox.getText().toString().trim();

                if(!text.isEmpty()) {
                    try {
                        textToSpeech.setLanguage(Locale.forLanguageTag(fromLangCode));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Speak(text);

                }

            }
        });
        binding.toAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.toTextbox.getText().toString().trim();

                if(!text.isEmpty()) {
                    try {
                        textToSpeech.setLanguage(Locale.forLanguageTag(toLangCode));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Speak(text);

                }
            }
        });


        
        return binding.getRoot();
    }
    private void ImageToText(Uri uri) throws IOException {

        InputImage inputImage = InputImage.fromFilePath(getContext(),uri);
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        Task<com.google.mlkit.vision.text.Text> textResult = textRecognizer.process(inputImage);

        textResult.addOnSuccessListener(new OnSuccessListener<com.google.mlkit.vision.text.Text>() {
            @Override
            public void onSuccess(com.google.mlkit.vision.text.Text text) {

                ProcessText(text);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ProcessText(com.google.mlkit.vision.text.Text text) {
        binding.fromTextbox.setText("");
        for (com.google.mlkit.vision.text.Text.TextBlock block : text.getTextBlocks()) {
            Point[] point = block.getCornerPoints();
            Rect rect = block.getBoundingBox();
            String textBlock = block.getText();

            for (com.google.mlkit.vision.text.Text.Line line : block.getLines()) {
                Point[] pointLine = line.getCornerPoints();
                Rect rectLine = line.getBoundingBox();
                String textBlockLine = line.getText();
                for (com.google.mlkit.vision.text.Text.Element element : line.getElements()) {
                    Point[] pointEl = element.getCornerPoints();
                    Rect rectEl = element.getBoundingBox();

                    binding.fromTextbox.append(element.getText() + " ");
                }
            }
            binding.fromTextbox.append("\n");
        }
    }

    private void Speak(String text) {


        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);





    }

    private void TranslateNow(String text, String fromLangCode, String toLangCode) {


        try{
            binding.toTextbox.setText("Please wait...");
            translatorOptions = new TranslatorOptions.Builder()
                    .setSourceLanguage(fromLangCode)
                    .setTargetLanguage(toLangCode).build();

            translator = Translation.getClient(translatorOptions);

            conditions = new DownloadConditions.Builder().build();
            getLifecycle().addObserver(translator);
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    binding.toTextbox.setText("Translating...");
                    translator.translate(text)
                            .addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    binding.toTextbox.setText(s);
                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "There is some problem", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    

    private String GetLangCode(String s) {

        String langCode;
        switch (s)
        {
            case "English" :
                langCode = TranslateLanguage.ENGLISH;
                break;
            case "Spanish":
                langCode = TranslateLanguage.SPANISH;
                break;
            case "French":
                langCode = TranslateLanguage.FRENCH;
                break;
            case "German":
                langCode = TranslateLanguage.GERMAN;
                break;
            case "Italian":
                langCode = TranslateLanguage.ITALIAN;
                break;
            case "Portuguese":
                langCode = TranslateLanguage.PORTUGUESE;
                break;
            case "Dutch":
                langCode = TranslateLanguage.DUTCH;
                break;
            case "Russian":
                langCode = TranslateLanguage.RUSSIAN;
                break;
            case "Chinese":
                langCode = TranslateLanguage.CHINESE;
                break;
            case "Japanese":
                langCode = TranslateLanguage.JAPANESE;
                break;
            case "Korean":
                langCode = TranslateLanguage.KOREAN;
                break;
            case "Arabic":
                langCode = TranslateLanguage.ARABIC;
                break;
            case "Turkish":
                langCode = TranslateLanguage.TURKISH;
                break;
            case "Hindi":
                langCode = TranslateLanguage.HINDI;
                break;
            case "Bengali":
                langCode = TranslateLanguage.BENGALI;
                break;
            case "Urdu":
                langCode = TranslateLanguage.URDU;
                break;
            case "Malay":
                langCode = TranslateLanguage.MALAY;
                break;
            case "Thai":
                langCode = TranslateLanguage.THAI;
                break;
            case "Marathi":
                langCode = TranslateLanguage.MARATHI;
                break;
            case "Kannada":
                langCode = TranslateLanguage.KANNADA;
                break;
            case "Telugu":
                langCode = TranslateLanguage.TELUGU;
                break;
            case "Tamil":
                langCode = TranslateLanguage.TAMIL;
                break;
            default:langCode = null;
        }
        return langCode;
    }


    @Override
    public void onPause() {
        super.onPause();

        if(textToSpeech.isSpeaking())
        {
            textToSpeech.stop();
        }
    }



}