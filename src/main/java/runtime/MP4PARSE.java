package runtime;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.List;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.DataInformationBox;
import com.coremedia.iso.boxes.DataReferenceBox;
import com.coremedia.iso.boxes.FileTypeBox;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaInformationBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.coremedia.iso.boxes.TrackReferenceBox;
import com.coremedia.iso.boxes.VideoMediaHeaderBox;
import com.coremedia.iso.boxes.mdat.MediaDataBox;

/**
 * Author: Chris Buckner, Driver for mp4 parse API
 * 
 */
public class MP4PARSE {
	public static void main(String[] args) {

		String TAB = "";

		if (args.length == 0) {
			System.out
					.println("Error: Bad command or filename. Syntax: java [filename.tpl]");
			System.exit(0);
		}

		IsoFile isoFile = null;
		try {
			isoFile = new IsoFile(args[0]);
		} catch (IOException e) {
			System.out.println("Could not read filename");
			e.printStackTrace();
		}
		List<Box> boxes = isoFile.getBoxes();
		System.out.println("[Box Overview]");
		for (Box box : boxes) {

			System.out.println("Type " + box.getType());
			System.out.println("Offset " + box.getOffset());
			System.out.println("Size " + box.getSize());
			System.out.println("Class " + box.getClass().toString());

		}

		FileTypeBox ftyp = isoFile.getBoxes(FileTypeBox.class).get(0);
		System.out.println();
		System.out.println("[Ftyp]");
		System.out.println(ftyp.toString());

		MovieBox moov = isoFile.getMovieBox();

		MovieHeaderBox mhb = moov.getMovieHeaderBox();
		System.out.println();
		System.out.println(new String(new char[1]).replace("\0", TAB)
				+ "[Moov]");
		System.out.println(new String(new char[1]).replace("\0", TAB)
				+ "moov track count: " + moov.getTrackCount());
		System.out.println(mhb.toString());

		List<TrackBox> trackBoxes = moov.getBoxes(TrackBox.class);

		System.out.println(new String(new char[1]).replace("\0", TAB)
				+ "Total trackBoxes " + trackBoxes.size());

		for (TrackBox trackBox : trackBoxes) {
			System.out.println();
			System.out.println(new String(new char[2]).replace("\0", TAB)
					+ "[track]");

			TrackHeaderBox tkhd = trackBox.getTrackHeaderBox();

			if (tkhd != null)
				System.out.println(new String(new char[3]).replace("\0", TAB)
						+ "[tkhd]" + tkhd.toString());

			List<TrackReferenceBox> trefs = trackBox
					.getBoxes(TrackReferenceBox.class);
			TrackReferenceBox tref = null;

			if (trefs.size() > 1)
				tref = trefs.get(0);

			if (tref != null)
				System.out.println(new String(new char[3]).replace("\0", TAB)
						+ "[tref]" + tref.toString());

			List<MediaBox> mdias = trackBox.getBoxes(MediaBox.class);

			MediaBox mdia = null;

			if (mdias.size() > 1)
				mdia = mdias.get(0);

			if (mdia != null) {
				System.out.println(new String(new char[3]).replace("\0", TAB)
						+ "[mdia]" + mdia.toString());

				System.out.println(new String(new char[4]).replace("\0", TAB)
						+ "[mdhd]" + mdia.getMediaHeaderBox().toString());

				List<MediaInformationBox> minfs = mdia
						.getBoxes(MediaInformationBox.class);

				MediaInformationBox minf = null;

				if (minfs.size() > 1)
					minf = minfs.get(0);

				if (minf != null) {
					System.out.println(new String(new char[4]).replace("\0",
							TAB) + "[minf]" + minf.toString());

					List<VideoMediaHeaderBox> vmhds = minf
							.getBoxes(VideoMediaHeaderBox.class);

					VideoMediaHeaderBox vmhd = null;

					if (vmhds.size() > 1)
						vmhd = vmhds.get(0);

					if (vmhd != null)
						System.out.println(new String(new char[5]).replace(
								"\0", TAB) + "[vmhd]" + vmhd.toString());

					List<SoundMediaHeaderBox> smhds = minf
							.getBoxes(SoundMediaHeaderBox.class);

					SoundMediaHeaderBox smhd = null;

					if (smhds.size() > 1)
						smhd = smhds.get(0);

					if (smhd != null)
						System.out.println(new String(new char[5]).replace(
								"\0", TAB) + "[smhd]" + smhd.toString());

					List<DataInformationBox> dinfs = minf
							.getBoxes(DataInformationBox.class);

					DataInformationBox dinf = null;

					if (dinfs.size() > 1)
						dinf = dinfs.get(0);

					if (dinf != null)
						System.out.println(new String(new char[5]).replace(
								"\0", TAB) + "[dinf]" + dinf.toString());

					List<DataReferenceBox> drefs = dinf
							.getBoxes(DataReferenceBox.class);

					DataReferenceBox dref = null;

					if (drefs.size() > 1)
						dref = drefs.get(0);

					if (dref != null)
						System.out.println(new String(new char[6]).replace(
								"\0", TAB) + "[dref]" + dref.toString());
				}
			}

			SampleTableBox stbl = trackBox.getSampleTableBox();

			if (stbl != null)
				System.out.println(new String(new char[5]).replace("\0", TAB)
						+ "[stbl]" + stbl.toString());

		}

		MediaDataBox mdat = isoFile.getBoxes(MediaDataBox.class).get(0);
		System.out.println();
		System.out.println("[Mdat]");
		System.out.println(mdat.toString());

	}
}
