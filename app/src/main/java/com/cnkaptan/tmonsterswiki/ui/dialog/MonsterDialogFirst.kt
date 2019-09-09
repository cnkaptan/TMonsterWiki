package com.cnkaptan.tmonsterswiki.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cnkaptan.tmonsterswiki.R
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterDialogAdapter


class MonsterDialogFirst : DialogFragment() {

    private var mMonsterList: List<MonsterEntity>? = null
    private var onFragmentInteractionListener: OnFragmentInteractionListener? = null
    private lateinit var mMonsterDialogAdapter: MonsterDialogAdapter
    private lateinit var rvDialog: RecyclerView


    val TAG: String
        get() = MonsterDialogFirst::class.java.simpleName

    companion object {
        fun newInstance(monsterList: List<MonsterEntity>): MonsterDialogFirst {
            val ARG_KEY_MONSTER = "MONSTER"
            val args = Bundle()
            args.putParcelableArrayList(
                MonsterDialogFirst::class.java.simpleName,
                ArrayList(monsterList)
            )
            val fragment = MonsterDialogFirst()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mMonsterList = arguments?.getParcelableArrayList(TAG)
    }

    override fun onStart() {
        super.onStart()
        try {
            onFragmentInteractionListener = activity as OnFragmentInteractionListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater!!.inflate(R.layout.dialog_monster, container, false)

        rvDialog = rootView.findViewById(R.id.rvMonsterList)

        Log.e("CheckFragment", "FirstDialog")

        rvDialog.setLayoutManager(LinearLayoutManager(this.activity))

        mMonsterDialogAdapter = MonsterDialogAdapter(mMonsterList!!, context!!) {
            Log.e("CheckFirstMonster", it.name)
            onFragmentInteractionListener?.postFirstMonster(it)
            dialog!!.dismiss()
        }
        rvDialog.adapter = mMonsterDialogAdapter

        return rootView
    }


    interface OnFragmentInteractionListener {
        fun postFirstMonster(monster: MonsterEntity)
    }
}
