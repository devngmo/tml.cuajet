package tml.cuajet.activities

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import tml.cuajet.R
import tml.cuajet.databinding.ActivitySimpleTreeEditorBinding
import tml.libs.cku.data.DataHub
import tml.libs.cku.data.DataHubResponseInterface
import tml.libs.cku.data.TreeNodeInterface
import java.util.*

class SimpleTreeEditorActivity : AppCompatActivity() {
    companion object {
        const val ARG_MODEL_ADAPTER = "model.adapter"
    }

    var modelAdapter:  TreeEditModelAdapter<TreeNodeInterface>? = null
    lateinit var curNode: TreeNodeInterface

    var itemLayoutID = R.layout.treenode_listitem
    lateinit var lvSTENodes: RecyclerView
    lateinit var binding: ActivitySimpleTreeEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimpleTreeEditorBinding.inflate(layoutInflater)
        val view = binding.root
        lvSTENodes = binding.root.findViewById(R.id.lvSTENodes)
        setContentView(view)
        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //   .setAction("Action", null).show()
        }

        //lvSTENodes.adapter = createNodeListAdapter()

        DataHub.get(ARG_MODEL_ADAPTER)?.let {
            modelAdapter = it as TreeEditModelAdapter<TreeNodeInterface>
            modelAdapter!!.curNode = modelAdapter!!.root

            lvSTENodes.adapter = it.createChildListAdapter()
        }
        if (modelAdapter == null) {
            Toast.makeText(this, "TreeEditModelAdapter not found on DataHub", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (modelAdapter!!.moveUp())
                return true
            else
                finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    interface TreeEditModelAdapter<TNode: TreeNodeInterface> {
        fun createChildListAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>?
        fun moveUp():Boolean {
            if (curNode!!.parent != null) {
                curNode = curNode!!.parent as TNode?
                return true
            }
            return false
        }

        var root : TNode?
        var curNode: TNode?
    }
}