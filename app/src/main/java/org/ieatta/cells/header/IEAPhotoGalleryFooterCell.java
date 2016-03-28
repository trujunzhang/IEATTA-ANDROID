package org.ieatta.cells.header;

import android.view.View;
import android.widget.Button;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.SectionTitleCellModel;


public class IEAPhotoGalleryFooterCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAPhotoGalleryFooterCell.class, R.layout.businesspage_section_footer);
    }

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.footer;
    }
    private Button footerLargeButton;

    public IEAPhotoGalleryFooterCell(View itemView) {
        super(itemView);

        this.footerLargeButton = (Button) itemView.findViewById(R.id.footer_large_button);
        this.footerLargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                this.model.viewController.presentPhotoGallery(0);
            }
        });
    }

    @Override
    public void render(Object value) {
//        this.model = (SectionPhotoGalleryFooterCellModel) value;

        // update UI
//        int photosCount = this.model.photosCount;
//        String title = EnvironmentUtils.sharedInstance.getGlobalContext().getResources().getString(R.string.See_All_Photos) + " " + photosCount;
//        this.footerLargeButton.setText(title);
//
//        if(photosCount == 0){
//            this.footerLargeButton.setEnabled(false);
//            this.footerLargeButton.setText(R.string.No_Photos);
//        }
    }

}