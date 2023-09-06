package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test.databinding.FragmentDetailBinding


const val ROW_POSITION = "ROW_POSITION"

class DetailFragment : Fragment() {
    private var _binding:FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        arguments?.let{
            position = it.getInt(ROW_POSITION)
        }

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View?{
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        val view = binding.root

//         ボタンクラスのインスタンス作成
        val btnListener = BtnListener()
        val btnListener_back = BtnListener_Back()
//         リスナを設定
        binding.Play.setOnClickListener(btnListener)
        binding.back.setOnClickListener(btnListener_back)


        return  view
//        return binding.root
    }

    override fun onViewCreated(view:View,savedInstanceState:Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val sights = getSights(resources)
        binding.detailKind.text = sights[position].name
        binding.detailName.text = sights[position].name
        binding.detiailDescription.text = sights[position].detail
        var img = resources.getIdentifier(sights[position].imageName,
                "drawable",context?.packageName)
        binding.detailImage.setImageResource(img)
    }

    //binding 解放
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // クリックリスナ
    private inner class BtnListener: View.OnClickListener{
        override fun onClick(v: View) {
            when(v.id){
                binding.Play.id -> {
                    if(position == 0){
                        var intent = Intent(context, BlackJack::class.java)
                        startActivity(intent)
                    }
                    else if(position == 1){
                        var intent = Intent(context, Quiz::class.java)
                        startActivity(intent)
                    }
                    else if(position == 2){
                        var intent = Intent(context, High_Low::class.java)
                        startActivity(intent)
                    }
                    else if(position == 3){
                        var intent = Intent(context, osero::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    // クリックリスナ
    private inner class BtnListener_Back: View.OnClickListener{
        override fun onClick(v: View) {
            when(v.id){
                binding.back.id -> {
                    var intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}