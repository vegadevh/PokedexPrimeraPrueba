package com.veegadiego.pokedex

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.veegadiego.pokedex.data.Pokemon
import kotlinx.android.synthetic.main.activity_main.*
import com.veegadiego.pokedex.api.NetworkUtils
import android.os.AsyncTask
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mEditSearch = findViewById<EditText>(R.id.et_buscar)
        val mBotonSearch = findViewById<Button>(R.id.bt_buscar)
        val mImageSearch = findViewById<ImageView>(R.id.poke_image)
        val mResultText= findViewById<RecyclerView>(R.id.rv_pokemon_list)


        bindView()

        mBotonSearch.setOnClickListener { view ->
            val et_pokemondata = mEditSearch.getText().toString().trim()
            if (et_pokemondata.isEmpty()) {
            } else {

                FetchPokemonTask().execute(et_pokemondata)
            }
        }


        mBotonSearch.setOnClickListener(View.OnClickListener {
            var meditable = mEditSearch.text.toString()

            if (meditable.trim().length>0){
                mImageSearch.setImageResource(R.drawable.poke_open)
            }
            else{
                mImageSearch.setImageResource(R.drawable.poke_close)
            }
        })

        initRecycler()
    }

    fun initRecycler(){

        var pokemon: MutableList<Pokemon> = MutableList(100){i ->
            Pokemon("Name: "+i, "Type: " + i)
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = PokemonAdapter(pokemon)

        rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    //METODOS.

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    fun bindView() {
        val mEditText_poke = findViewById<EditText>(R.id.et_buscar)
        val mBotonSearchh = findViewById<Button>(R.id.bt_buscar)
        val mResultText = findViewById<RecyclerView>(R.id.rv_pokemon_list)
    }


    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg pokemonNumbers: String): String? {

            if (pokemonNumbers.size == 0) {
                return null
            }

            val ID = pokemonNumbers[0]

            val pokeAPI = NetworkUtils.buildUrl(ID)

            try {
                return NetworkUtils.getResponseFromHttpUrl(pokeAPI!!)
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        }

        override fun onPostExecute(pokemonInfo: String?) {
            val mResultText = findViewById<RecyclerView>(R.id.rv_pokemon_list)
        }
    }
}
