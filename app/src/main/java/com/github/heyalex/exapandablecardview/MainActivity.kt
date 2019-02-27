package com.github.heyalex.exapandablecardview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.heyalex.expandable_cardview.ExpandableCardView

class MainActivity : AppCompatActivity() {

    private var expandedCard: ExpandableCardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        expandedCard = findViewById(R.id.expanded_card)
        expandedCard?.setOnExpandChangeListener {
            Toast.makeText(
                applicationContext,
                if (it) { "Expanded" } else { "Collapsed" },
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
