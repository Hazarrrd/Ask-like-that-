package com.example.asklikethat.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.CountDownTimer
import android.widget.Button
import com.example.asklikethat.R
import kotlinx.android.synthetic.main.fragment_rapid_things.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RapidThingsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RapidThingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RapidThingsFragment : Fragment() {

    private var isClicked = false
    var cTimer: CountDownTimer? = null
    lateinit var button:Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startTimer()
        val view = inflater.inflate(R.layout.fragment_rapid_things, container, false)
        button = view.findViewById(R.id.button3) as Button
        button.setOnClickListener { onClick(it) }
        // Inflate the layout for this fragment
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelTimer()
    }

    fun blockButton() {
        button.isClickable = false
    }

    fun unblockButton() {
        button.isClickable = true
    }


    fun startTimer() {
        cTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                textView2.setText("Time: " + millisUntilFinished / 1000)
            }
            override fun onFinish() {
                textView2.setText("Time: 0")

                (activity as RapidGameActivity).handleEndGame()
            }
        }
        (cTimer as CountDownTimer).start()
    }

    fun cancelTimer() {
        if (cTimer != null)
            cTimer!!.cancel()
    }

    private fun onClick(it: View?) {
        if(!isClicked) {
            isClicked = true

            (activity as RapidGameActivity).skip()
            isClicked = false

        }
    }





}
